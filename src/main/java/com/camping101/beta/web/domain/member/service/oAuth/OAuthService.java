package com.camping101.beta.web.domain.member.service.oAuth;

import com.camping101.beta.web.domain.member.dto.TokenInfo;

public interface OAuthService {

    TokenInfo signInByOAuth(String code);
    TokenInfo reissueAccessTokenByRefreshToken(String previousAccessToken);
    void revokeAccessTokenForLogOut(String authorization);

}
