package com.camping101.beta.web.domain.token.service;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.util.JwtProvider;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.token.RefreshToken;
import com.camping101.beta.web.domain.token.exception.ErrorCode;
import com.camping101.beta.web.domain.token.exception.TokenException;
import com.camping101.beta.web.domain.token.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Optional;
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

    public String createAccessToken(MemberDetails memberDetails) throws MemberException{

        MemberType memberType = memberDetails.getMemberType().orElseThrow(() -> new MemberException(MEMBER_TYPE_NOT_FOUND));

        return createAccessToken(memberDetails.getEmail(), memberDetails.getMemberId(), memberType);
    }

    public String createAccessToken(Member member){

        String rawAccessToken = jwtProvider.createToken(member, accessExpireMillisecond);

        log.info("TokenProvider.createAccessToken : {}", rawAccessToken);

        return String.format("%s%s", ACCESS_TOKEN_PREFIX, rawAccessToken);
    }

    public String createAccessToken(String email, Long memberId, MemberType memberType){

        String rawAccessToken = jwtProvider.createToken(email, memberId, memberType, accessExpireMillisecond);

        log.info("TokenProvider.createAccessToken : {}", rawAccessToken);

        return String.format("%s%s", ACCESS_TOKEN_PREFIX, rawAccessToken);
    }

    public String createAccessTokenByRefreshToken(String refreshToken) {

        String rawRefreshToken = refreshToken.substring(REFRESH_TOKEN_PREFIX.length() - 1);

        RefreshToken rt = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenException(ErrorCode.INVALID_REFRESH_TOKEN));

        return createAccessToken(rt.getEmail(), rt.getMemberId(), MemberType.valueOf(rt.getMemberType()));

    }

    public String createRefreshToken(MemberDetails memberDetails) throws MemberException{

        MemberType memberType = memberDetails.getMemberType().orElseThrow(() -> new MemberException(MEMBER_TYPE_NOT_FOUND));

        return createRefreshToken(memberDetails.getEmail(), memberDetails.getMemberId(), memberType);
    }

    public String createRefreshToken(String email, Long memberId, MemberType memberType){

        String rawRefreshToken = jwtProvider.createToken(email, memberId, memberType, refreshExpireMillisecond);

        log.info("TokenProvider.createRefreshToken : {}", rawRefreshToken);

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

        return createRefreshToken(googleRefreshToken, member.getEmail(), member.getMemberId(), member.getMemberType());
    }

    public String createRefreshToken(String googleRefreshToken, String email, Long memberId, MemberType memberType){

        String rawRefreshToken = jwtProvider.createToken(email, memberId, memberType, refreshExpireMillisecond);

        log.info("TokenProvider.createRefreshToken : {}", rawRefreshToken);

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

    public Long extractMemberIdByAccessToken(String accessToken) throws MalformedJwtException, ExpiredJwtException, SignatureException, UnsupportedJwtException{

        Claims claims = jwtProvider.getClaim(accessToken);

        return jwtProvider.getMemberId(claims);
    }

    public Long extractMemberIdByRefreshToken(String refreshToken) throws TokenException{

        RefreshToken rt = findRefreshTokenFromRedis(refreshToken)
                .orElseThrow(() -> new TokenException(ErrorCode.INVALID_REFRESH_TOKEN));

        return rt.getMemberId();
    }

    public Authentication generateAuthenticationByAccessToken(String accessToken) throws MalformedJwtException, ExpiredJwtException, SignatureException, UnsupportedJwtException {
        return jwtProvider.getAuthentication(accessToken);
    }

    public boolean notEmptyAndStartsWithBearer(String accessToken) {

        if (!StringUtils.hasText(accessToken)) return false;

        return accessToken.startsWith(ACCESS_TOKEN_PREFIX);
    }

    public boolean notEmptyAndStartsWithBasic(String refreshToken) {

        if (!StringUtils.hasText(refreshToken)) return false;

        return refreshToken.startsWith(REFRESH_TOKEN_PREFIX);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        return refreshTokenRepository.existsByRefreshToken(refreshToken);
    }

}
