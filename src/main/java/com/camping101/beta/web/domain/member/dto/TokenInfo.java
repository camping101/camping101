package com.camping101.beta.web.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class TokenInfo {

    private String accessToken;
    private String refreshToken;

    public static TokenInfo of(String rawAccessToken, String rawRefreshToken){
        return TokenInfo.builder()
                .accessToken(String.format("Bearer %s", rawAccessToken))
                .refreshToken(String.format("Basic %s", rawRefreshToken))
                .build();
    }

}
