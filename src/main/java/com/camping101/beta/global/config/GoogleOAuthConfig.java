package com.camping101.beta.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 구글 OAuth 설정 정보
 */

@Configuration
public class GoogleOAuthConfig {

    public static String googleClientId;
    public static String googleClientSecret;
    public static String googleRedirectUri;
    public static String googleTokenUri;
    public static String googleAuthorizationUri;
    public static String googleRevokeUri;
    public static String googleScope;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    public void setClientId(String clientId) {
        GoogleOAuthConfig.googleClientId = clientId;
    }

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    public void setClientSecret(String clientSecret) {
        GoogleOAuthConfig.googleClientSecret = clientSecret;
    }

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    public void setRedirectUri(String redirectUri) {
        GoogleOAuthConfig.googleRedirectUri = redirectUri;
    }

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    public void setGoogleTokenUri(String googleTokenUri) {
        GoogleOAuthConfig.googleTokenUri = googleTokenUri;
    }

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    public void setAuthorizationUri(String authorizationUri) {
        GoogleOAuthConfig.googleAuthorizationUri = authorizationUri;
    }

    @Value("${oauth2.client.provider.google.all-token-revoke-uri}")
    public void setGoogleRevokeUri(String googleRevokeUri) {
        GoogleOAuthConfig.googleRevokeUri = googleRevokeUri;
    }

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    public void setGoogleScope(String googleScope) {
        GoogleOAuthConfig.googleScope = googleScope;
    }
}
