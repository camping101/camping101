package com.camping101.beta.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${token.jwt.singing.key}")
    private String signingKey;
    private Logger logger = Logger.getLogger(JwtProvider.class.getName());

    public String createToken(String email, List<String> memberType, long expiredSecond) {

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("email", email);

        if (!CollectionUtils.isEmpty(memberType)) {
            payloads.put("memberType", memberType.stream().findFirst().get());
        }

        long expiredTime = new Date().getTime() + expiredSecond;

        logger.info("JwtProvider: Jwt 토큰 생성 >> 만료일은 " + new Date(expiredTime));

        return Jwts.builder()
                .setClaims(payloads)
                .setExpiration(new Date(expiredTime))
                .signWith(SignatureAlgorithm.HS256, signingKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Claims getClaim(String jwtToken)
            throws MalformedJwtException, ExpiredJwtException,
            SignatureException, UnsupportedJwtException {

        return Jwts.parser()
                .setSigningKey(signingKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public Claims getClaim(String jwtToken, String key)
            throws MalformedJwtException, ExpiredJwtException,
            SignatureException, UnsupportedJwtException {

        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public String getEmail(Claims claim) {
        return claim.get("email", String.class);
    }

    public List<String> getMemberType(Claims claim) {
        return List.of(claim.get("memberType", String.class));
    }

}
