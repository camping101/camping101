package com.camping101.beta.security;

import com.camping101.beta.member.entity.type.SignUpType;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
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
        payloads.put("memberType", memberType);

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

    public String getEmail(Claims claim) {
        return claim.get("email", String.class);
    }

    public List<String> getMemberType(Claims claim) {
        return (List<String>) claim.get("memberType", Object.class);
    }

}
