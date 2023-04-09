package com.camping101.beta.global.security.authentication;

import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.util.FilterResponseHandler;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String accessToken = tokenService.createAccessToken(memberDetails);
        String refreshToken = tokenService.createRefreshToken(memberDetails);
        tokenService.setTokenHeader(response, accessToken, refreshToken);
        FilterResponseHandler.sendSuccessResponse(response, tokenService.tokenBody(accessToken, refreshToken));

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
