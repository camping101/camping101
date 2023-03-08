package com.camping101.beta.member.service.oAuth;

import com.camping101.beta.member.dto.TokenInfo;

public interface OAuthService {

    TokenInfo signInByOAuth(String code);
    TokenInfo renewToken(String previousAccessToken, String previousRefreshToken);
    void revokeAccessTokenForLogOut(String authorization);

}
