package com.camping101.beta.web.domain.member.service;

import com.camping101.beta.web.domain.member.dto.MemberSignInRequest;
import com.camping101.beta.web.domain.member.dto.TokenInfo;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.TemporalPassword;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.repository.TemporalPasswordRepository;
import com.camping101.beta.web.domain.member.service.oAuth.OAuthService;
import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.web.domain.token.service.TokenService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSignInServiceImpl implements MemberSignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemporalPasswordRepository temporalPasswordRepository;
    private final OAuthService googleOAuthService;
    private final TokenService tokenService;

    @Value("${temporal.password.expiration.seconds}")
    private long TEMPORAL_PASSWORD_EXPIRATION_TIME;

    @Override
    public void authenticateEmailMember(MemberSignInRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("MEMBER NOT FOUND"));

        if (!isPasswordMatching(member, request.getPassword())) {
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }

    }

    private boolean isPasswordMatching(Member member, String requestPassword) {

        Optional<TemporalPassword> optionalTemporalPassword
                = temporalPasswordRepository.findByMemberId(member.getMemberId());

        if (optionalTemporalPassword.isPresent()) {

            TemporalPassword temporalPassword = optionalTemporalPassword.get();
            validateIfTemporalPasswordExpired(temporalPassword);
            return StringUtils.equals(temporalPassword.getTemporalPassword(), requestPassword);

        } else {
            return passwordEncoder.matches(requestPassword, member.getPassword());
        }
    }

    private void validateIfTemporalPasswordExpired(TemporalPassword temporalPassword) {

        LocalDateTime expirationTime = temporalPassword.getCreatedAt().plusSeconds(TEMPORAL_PASSWORD_EXPIRATION_TIME);

        if (expirationTime.isBefore(LocalDateTime.now())) {
            throw new MemberException(ErrorCode.TEMPORAL_PASSWORD_EXPIRED);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR));

        return new MemberDetails(member);
    }

    @Override
    public TokenInfo refreshToken(String refreshToken) {

        try {

            Member member = memberRepository.findById(tokenService.extractMemberIdByRefreshToken(refreshToken))
                    .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR));

            return /*SignUpType.EMAIL.equals(member.getSignUpType()) ?
                    reissueEmailMemberAccessToken(member) :*/ reissueGoogleMemberAccessToken(refreshToken);

        } catch (ExpiredJwtException e) {
            log.info("MemberSignInServiceImpl.refreshToken : Refresh Token도 만료되었네요. 다시 로그인 하시죠..");
            throw new MemberException(ErrorCode.REFRESH_TOKEN_INVALID);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            log.info("MemberSignInServiceImpl.refreshToken : Refresh Token 파싱이 되지 않습니다. 다시 로그인 하시죠..");
            throw new MemberException(ErrorCode.REFRESH_TOKEN_INVALID);
        } catch (Exception e) {
            log.warn("MemberSignInServiceImp.refreshToken : " + e.getMessage());
            throw new MemberException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

    }

    private TokenInfo reissueGoogleMemberAccessToken(String serverRefreshToken) {
        TokenInfo newTokenInfo = googleOAuthService.reissueAccessTokenByRefreshToken(serverRefreshToken);
        log.info("토큰 갱신 완료 >> 새로운 AccessToken : " + newTokenInfo.getAccessToken());
        return newTokenInfo;
    }

//    private TokenInfo reissueEmailMemberAccessToken(Member member) {
//
//        tokenService.createAccessToken(member);
//
//
//        log.info("토큰 갱신 완료 >> 새로운 AccessToken : Bearer {}", newRawServerAccessToken);
//
//        return new TokenInfo(newRawServerAccessToken, newRawServerRefreshToken);
//    }

}
