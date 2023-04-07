package com.camping101.beta.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 구글 OAuth 설정 정보
 */

@Configuration
public class GoogleOAuthConfig {

    public static String clientId;
    public static String clientSecret;
    public static String redirectUri;
    public static String googleTokenUri;  // 엑세스 토큰 생성 및 갱신
    public static String googleRevokeUri;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    public void setClientId(String clientId) {
        GoogleOAuthConfig.clientId = clientId;
    }

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    public void setClientSecret(String clientSecret) {
        GoogleOAuthConfig.clientSecret = clientSecret;
    }

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    public void setRedirectUri(String redirectUri) {
        GoogleOAuthConfig.redirectUri = redirectUri;
    }

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    public void setGoogleTokenUri(String googleTokenUri) {
        GoogleOAuthConfig.googleTokenUri = googleTokenUri;
    }

    @Value("${oauth2.client.provider.google.all-token-revoke-uri}")
    public void setGoogleRevokeUri(String googleRevokeUri) {
        GoogleOAuthConfig.googleRevokeUri = googleRevokeUri;
    }
}
