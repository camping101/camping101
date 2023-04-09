package com.camping101.beta.web.domain.member.service.signin;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.service.oAuth.OAuthService;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import com.camping101.beta.web.domain.member.service.temporalPassword.TemporalPasswordServiceImpl;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.camping101.beta.web.domain.member.exception.ErrorCode.INVALID_REFRESH_TOKEN;
import static com.camping101.beta.web.domain.member.exception.ErrorCode.MEMBER_SIGN_IN_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSignInServiceImpl implements MemberSignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemporalPasswordServiceImpl temporalPasswordService;
    private final OAuthService googleOAuthService;
    private final TokenService tokenService;

    @Override
    public MemberDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        return new MemberDetails(member);
    }

    @Override
    public boolean isPasswordMatching(MemberDetails memberDetails, String rawPassword) {

        if (temporalPasswordService.isTemporalPasswordMatching(memberDetails.getMemberId(), rawPassword)) {
            log.info("MemberSignInServiceImpl.isPasswordMatching : 임시 비밀번호가 일치합니다.");
            return true;
        }

        return passwordEncoder.matches(rawPassword, memberDetails.getPassword());
    }

    @Override
    public TokenInfo refreshToken(String refreshToken) {

        try {

            Member member = memberRepository.findById(tokenService.extractMemberIdByRefreshToken(refreshToken))
                    .orElseThrow(() -> new MemberException(MEMBER_SIGN_IN_ERROR));

            return /*SignUpType.EMAIL.equals(member.getSignUpType()) ?
                    reissueEmailMemberAccessToken(member) :*/ reissueGoogleMemberAccessToken(refreshToken);

        } catch (ExpiredJwtException e) {
            log.info("MemberSignInServiceImpl.refreshToken : Refresh Token도 만료되었네요. 다시 로그인 하시죠..");
            throw new MemberException(INVALID_REFRESH_TOKEN);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            log.info("MemberSignInServiceImpl.refreshToken : Refresh Token 파싱이 되지 않습니다. 다시 로그인 하시죠..");
            throw new MemberException(INVALID_REFRESH_TOKEN);
        } catch (Exception e) {
            log.warn("MemberSignInServiceImp.refreshToken : " + e.getMessage());
            throw new MemberException(INVALID_REFRESH_TOKEN);
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
