package com.camping101.beta.web.domain.member.dto.signin.oAuth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleTokenInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
    private long expiresIn; // millisecond
    private String scope;
    private String tokenType;
    private String idToken;

}
