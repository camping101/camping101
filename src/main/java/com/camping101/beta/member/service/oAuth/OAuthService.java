package com.camping101.beta.member.service.oAuth;

import com.camping101.beta.member.dto.MemberSignInResponse;

public interface OAuthService {

    MemberSignInResponse createOrUpdateMemberWhenSignIn(String code);
    MemberSignInResponse renewToken(String previousAccessToken, String previousRefreshToken);
    void revokeAccessTokenForLogOut(String authorization);

}
