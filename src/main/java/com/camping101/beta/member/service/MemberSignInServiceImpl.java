package com.camping101.beta.member.service;

import com.camping101.beta.member.dto.MemberSignInRequest;
import com.camping101.beta.member.dto.TokenInfo;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.TemporalPassword;
import com.camping101.beta.member.entity.type.SignUpType;
import com.camping101.beta.member.exception.ErrorCode;
import com.camping101.beta.member.exception.MemberException;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.member.repository.TemporalPasswordRepository;
import com.camping101.beta.member.service.oAuth.OAuthService;
import com.camping101.beta.security.JwtProvider;
import com.camping101.beta.security.MemberDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MemberSignInServiceImpl implements MemberSignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemporalPasswordRepository temporalPasswordRepository;
    private final OAuthService googleOAuthService;
    private final JwtProvider jwtProvider;
    private final Logger logger = Logger.getLogger(MemberSignInServiceImpl.class.getName());

    @Value("${token.jwt.accesstoken}")
    private long ACCESS_EXPIRE_MILLISECOND; // 12시간
    @Value("${token.jwt.refreshtoken}")
    private long REFRESH_EXPIRE_MILLISECOND; // 3개월
    @Value("${temporal.password.expiration.seconds}")
    private long PASSWORD_RESET_TIME; // 임시 비밀번호 만료 - 5분

    @Override
    public void authenticateRequest(MemberSignInRequest request) throws AuthenticationException {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("MEMBER NOT FOUND"));

        Optional<TemporalPassword> optionalTemporalPassword
                = temporalPasswordRepository.findByEmail(request.getEmail());

        if (optionalTemporalPassword.isPresent()) {
            TemporalPassword temporalPassword = optionalTemporalPassword.get();
            if (temporalPassword.getTemporalPassword().equals(request.getPassword())) {
                validateIfTemporalPasswordCanBeActivated(temporalPassword);
                logger.info(member.getEmail() + " 의 임시 비밀번호는 : " + temporalPassword.getTemporalPassword());
            }
        } else {
            if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
                logger.info("MemberSignInServiceImpl : 비밀번호 불일치 (EMAIL : " + member.getEmail() + ")");
                throw new BadCredentialsException("BAD CREDENTIAL");
            }
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR));

        return new MemberDetails(member);
    }

    private void validateIfTemporalPasswordCanBeActivated(TemporalPassword temporalPassword){
        if (temporalPassword.isActiveYn()) {
            throw new MemberException(ErrorCode.MEMBER_RESET_PASSWORD_FAIL);
        }
        if (LocalDateTime.now().isAfter(temporalPassword.getEmailSentAt().plusSeconds(PASSWORD_RESET_TIME))) {
            throw new MemberException(ErrorCode.MEMBER_RESET_PASSWORD_FAIL);
        }
    }

    @Override
    public TokenInfo refreshToken(String serverAccessToken, String serverRefreshToken) {

        try {

            String refreshToken = serverRefreshToken.substring(6);
            Claims claim = jwtProvider.getClaim(refreshToken);
            String email = jwtProvider.getEmail(claim);

            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR));

            if (SignUpType.EMAIL.equals(member.getSignUpType())) {
                // 이메일 가입 회원의 경우 토큰을 새로 생성해준다.
                String newServerAccessToken = jwtProvider.createToken(
                        member.getEmail(), List.of(member.getMemberType().name()), ACCESS_EXPIRE_MILLISECOND);
                String newServerRefreshToken = jwtProvider.createToken(
                        member.getEmail(), List.of(member.getMemberType().name()), REFRESH_EXPIRE_MILLISECOND);
                logger.info("토큰 갱신 완료 >> 새로운 AccessToken : Bearer " + newServerAccessToken);
                return new TokenInfo("Bearer " + newServerAccessToken, "Basic " + newServerRefreshToken);
            } else {
                // 구글 가입 회원의 경우 구글의 Access Token 유효시간에 맞춰 토큰을 새로 생성해준다.
                TokenInfo newTokenInfo = googleOAuthService.renewToken(serverAccessToken, serverRefreshToken);
                logger.info("토큰 갱신 완료 >> 새로운 AccessToken : " + newTokenInfo.getAccessToken());
                return newTokenInfo;
            }
        } catch (ExpiredJwtException e) {
            logger.info("MemberSignInServiceImpl.refreshToken : Refresh Token도 만료되었네요. 다시 로그인 하시죠..");
            throw new MemberException(ErrorCode.MEMBER_TOKEN_REFRESH_ERROR);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            logger.info("MemberSignInServiceImpl.refreshToken : Refresh Token 파싱이 되지 않습니다. 다시 로그인 하시죠..");
            throw new MemberException(ErrorCode.MEMBER_TOKEN_REFRESH_ERROR);
        } catch (Exception e) {
            logger.warning("MemberSignInServiceImp.refreshToken : " + e.getMessage());
            throw new MemberException(ErrorCode.MEMBER_TOKEN_REFRESH_ERROR);
        }

    }

}
