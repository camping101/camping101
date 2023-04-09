package com.camping101.beta.web.domain.member.service.token;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.db.entity.token.RefreshToken;
import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.util.JwtProvider;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.exception.TokenException;
import com.camping101.beta.web.domain.member.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.camping101.beta.db.entity.member.type.SignUpType.GOOGLE;
import static com.camping101.beta.web.domain.member.exception.ErrorCode.INVALID_REFRESH_TOKEN;
import static com.camping101.beta.web.domain.member.exception.ErrorCode.MEMBER_TYPE_NOT_FOUND;

/**
 * Token Provider
 * - Access Token, Refresh Token 생성 및 Redis 저장 관리
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${token.jwt.accesstoken}")
    private long accessExpireMillisecond;
    @Value("${token.jwt.refreshtoken}")
    private long refreshExpireMillisecond;

    private final static String ACCESS_TOKEN_PREFIX = "Bearer ";
    private final static String REFRESH_TOKEN_PREFIX = "Basic ";

    public Optional<RefreshToken> findRefreshTokenFromRedis(String refreshToken){

        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    public Long extractMemberIdByAccessToken(String accessToken) throws MalformedJwtException, ExpiredJwtException, SignatureException, UnsupportedJwtException{

        Claims claims = jwtProvider.getClaim(accessToken);

        return jwtProvider.getMemberId(claims);
    }

    public Long extractMemberIdByRefreshToken(String refreshToken) throws TokenException{

        RefreshToken rt = findRefreshTokenFromRedis(refreshToken)
                .orElseThrow(() -> new TokenException(INVALID_REFRESH_TOKEN));

        return rt.getMemberId();
    }

    public String extractEmailByAccessToken(String accessToken) throws MalformedJwtException, ExpiredJwtException, SignatureException, UnsupportedJwtException{

        Claims claims = jwtProvider.getClaim(accessToken);

        return jwtProvider.getEmail(claims);
    }

    public void setTokenHeader(HttpServletResponse response,
                               String accessToken, String refreshToken) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
    }

    public Map<String, String> tokenBody(String accessToken, String refreshToken) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("access_token", accessToken);
        responseMap.put("refresh_token", refreshToken);
        return responseMap;
    }

    public String createAccessToken(MemberDetails memberDetails) throws MemberException{

        MemberType memberType = memberDetails.getMemberType().orElseThrow(() -> new MemberException(MEMBER_TYPE_NOT_FOUND));

        return createAccessToken(memberDetails.getEmail(), memberDetails.getMemberId(), memberType, memberDetails.getSignUpType());
    }

    public String createAccessToken(Member member){

        String rawAccessToken = jwtProvider.createToken(member, accessExpireMillisecond);

        log.info("TokenProvider.createAccessToken : Bearer {}", rawAccessToken);

        return String.format("%s%s", ACCESS_TOKEN_PREFIX, rawAccessToken);
    }

    public String createAccessToken(String email, Long memberId, MemberType memberType, SignUpType signUpType){

        String rawAccessToken = jwtProvider.createToken(email, memberId, memberType, signUpType, accessExpireMillisecond);

        log.info("TokenProvider.createAccessToken : Bearer {}", rawAccessToken);

        return String.format("%s%s", ACCESS_TOKEN_PREFIX, rawAccessToken);
    }

    public String createRefreshToken(MemberDetails memberDetails) throws MemberException{

        MemberType memberType = memberDetails.getMemberType().orElseThrow(() -> new MemberException(MEMBER_TYPE_NOT_FOUND));

        return createRefreshToken(memberDetails.getEmail(), memberDetails.getMemberId(), memberType, memberDetails.getSignUpType());
    }

    public String createRefreshToken(String email, Long memberId, MemberType memberType, SignUpType signUpType){

        String rawRefreshToken = jwtProvider.createToken(email, memberId, memberType, signUpType, refreshExpireMillisecond);

        log.info("TokenProvider.createRefreshToken : Basic {}", rawRefreshToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(rawRefreshToken)
                .memberId(memberId)
                .email(email)
                .memberType(memberType.name())
                .build();

        refreshTokenRepository.save(refreshToken);

        return String.format("%s%s", REFRESH_TOKEN_PREFIX, rawRefreshToken);
    }

    public String createRefreshToken(String googleRefreshToken, Member member) {

        return createRefreshToken(googleRefreshToken, member.getEmail(), member.getMemberId(), member.getMemberType(), GOOGLE);
    }

    public String createRefreshToken(String googleRefreshToken, String email, Long memberId, MemberType memberType, SignUpType signUpType){

        String rawRefreshToken = jwtProvider.createToken(email, memberId, memberType, signUpType, refreshExpireMillisecond);

        log.info("TokenProvider.createRefreshToken : Basic {}", rawRefreshToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(rawRefreshToken)
                .googleRefreshToken(googleRefreshToken)
                .memberId(memberId)
                .email(email)
                .memberType(memberType.name())
                .build();

        refreshTokenRepository.save(refreshToken);

        return String.format("%s%s", REFRESH_TOKEN_PREFIX, rawRefreshToken);
    }

    public boolean isNotBlankAndStartsWithBearer(String accessToken) {

        return StringUtils.hasText(accessToken) && accessToken.startsWith(ACCESS_TOKEN_PREFIX);
    }

    public boolean isNotEmptyAndStartsWithBasic(String refreshToken) {

        if (!StringUtils.hasText(refreshToken)) return false;

        return refreshToken.startsWith(REFRESH_TOKEN_PREFIX);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        return refreshTokenRepository.existsByRefreshToken(refreshToken);
    }

}
