package com.camping101.beta.web.domain.member.controller;

import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.web.domain.member.dto.token.MemberRefreshTokenRequest;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.service.oAuth.OAuthService;
import com.camping101.beta.web.domain.member.service.signin.MemberSignInService;
import com.camping101.beta.web.domain.member.service.temporalPassword.TemporalPasswordService;
import com.querydsl.core.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.camping101.beta.global.config.GoogleOAuthConfig.clientId;
import static com.camping101.beta.web.domain.member.exception.ErrorCode.INVALID_REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signin")
@Api(tags = {"캠핑 101 - 로그인 API"})
@Slf4j
public class MemberSignInController {

    private final MemberSignInService memberSignInService;
    private final OAuthService googleOAuthService;
    private final TemporalPasswordService temporalPasswordService;

    @PostMapping("/temporal-password")
    public ResponseEntity<Void> temporalPasswordSend(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails){

        temporalPasswordService.sendTemporalPassword(memberDetails.getMemberId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/google")
    @ApiOperation(value = "googleSignIn", notes = "!!!! 직접 URL에 입력해서 확인해주세요. !!!!")
    public void googleSignIn(@ApiIgnore HttpServletResponse response) throws IOException {

        System.out.println(clientId);

        response.sendRedirect("https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=https%3A//www.googleapis.com/auth/drive.metadata.readonly&" +
                "access_type=offline&" +
                "include_granted_scopes=true&" +
                "response_type=code&" +
                "redirect_uri=http://localhost:8080/api/signin/oauth/google&" +
                "client_id=" + clientId);

    }

    @GetMapping("/oauth/google") @ApiIgnore
    public ResponseEntity<TokenInfo> googleSignIn(@RequestParam String code,
                                                  HttpServletResponse response) {

        log.info("Auth Code : {}", code);

        TokenInfo tokenInfo = googleOAuthService.signInByOAuth(code);

        addAccessTokenAndRefreshTokenToResponseHeader(response, tokenInfo);

        return ResponseEntity.ok(tokenInfo);
    }

    private void addAccessTokenAndRefreshTokenToResponseHeader(HttpServletResponse response, TokenInfo tokenInfo) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("ACCESS_TOKEN", tokenInfo.getAccessToken());
        response.setHeader("REFRESH_TOKEN", tokenInfo.getRefreshToken());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenInfo> refreshTokenReissue(@RequestBody MemberRefreshTokenRequest tokenRequest,
                                                         @ApiIgnore HttpServletRequest request,
                                                         @ApiIgnore HttpServletResponse response){

        String refreshToken = tokenRequest.getRefreshToken();

        validateTokenRequest(refreshToken);

        TokenInfo tokenInfo = memberSignInService.refreshToken(refreshToken);

        addAccessTokenAndRefreshTokenToResponseHeader(response, tokenInfo);

        return ResponseEntity.ok(tokenInfo);
    }

    private void validateTokenRequest(String serverRefreshToken) {

        if (StringUtils.isNullOrEmpty(serverRefreshToken) || !serverRefreshToken.startsWith("Basic")) {

            log.info("MemberSignInController.refreshToken : Refresh Token 이 없거나 Basic으로 시작하지 않습니다.");

            throw new MemberException(INVALID_REFRESH_TOKEN);
        }

    }

}
