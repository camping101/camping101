package com.camping101.beta.member.service.oAuth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleTokenInfo {

    private String accessToken;
    private long expiresIn; // millisecond
    private String scope;
    private String tokenType;
    private String idToken;

}
