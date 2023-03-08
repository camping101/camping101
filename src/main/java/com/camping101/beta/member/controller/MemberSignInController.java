package com.camping101.beta.member.controller;

import com.camping101.beta.member.dto.MemberSignInRequest;
import com.camping101.beta.member.dto.MemberTokenRefresh;
import com.camping101.beta.member.dto.TokenInfo;
import com.camping101.beta.member.exception.ErrorCode;
import com.camping101.beta.member.exception.MemberException;
import com.camping101.beta.member.service.MemberSignInService;
import com.camping101.beta.member.service.oAuth.OAuthService;
import com.querydsl.core.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signin")
@Api(tags = {"로그인 API"})
public class MemberSignInController {

    private final MemberSignInService memberSignInService;
    private final OAuthService googleOAuthService;
    private final Logger logger = Logger.getLogger(MemberSignInController.class.getName());

    @PostMapping("/mail")
    public ResponseEntity<?> signInByMail(MemberSignInRequest memberSignInRequest) {

        memberSignInService.authenticateRequest(memberSignInRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/google")
    @ApiOperation(value = "signInByGoogle", notes = "!!!! 직접 URL에 입력해서 확인해주세요. !!!!")
    public void signInByGoogle(@ApiIgnore HttpServletResponse response) throws IOException {

        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/oauth/google")
    @ApiIgnore
    public ResponseEntity<TokenInfo> createOrUpdateMemberWhenSignIn(
            @RequestParam String code, @ApiIgnore HttpServletResponse response) {

        TokenInfo tokenInfo = googleOAuthService.signInByOAuth(code);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("ACCESS_TOKEN", tokenInfo.getAccessToken());
        response.setHeader("REFRESH_TOKEN",tokenInfo.getRefreshToken());

        return ResponseEntity.ok(tokenInfo);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenInfo> refreshToken(@RequestBody MemberTokenRefresh refreshToken,
                                                  @ApiIgnore HttpServletRequest request,
                                                  @ApiIgnore HttpServletResponse response){

        String serverAccessToken = request.getHeader("Authorization");
        String serverRefreshToken = refreshToken.getRefreshToken();

        if (StringUtils.isNullOrEmpty(serverAccessToken)
            || !serverAccessToken.startsWith("Bearer")
            || StringUtils.isNullOrEmpty(serverRefreshToken)
            || !serverRefreshToken.startsWith("Basic")) {

            logger.info("MemberSignInController.refreshToken : " +
                    "Header에 Access Token이 없거나, Body에 Refresh Token 이 없거나 Basic으로 시작하지 않습니다.");

            throw new MemberException(ErrorCode.MEMBER_TOKEN_REFRESH_ERROR);
        }

        TokenInfo tokenInfo = memberSignInService.refreshToken(serverAccessToken, serverRefreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("ACCESS_TOKEN", tokenInfo.getAccessToken());
        response.setHeader("REFRESH_TOKEN", tokenInfo.getRefreshToken());

        return ResponseEntity.ok(tokenInfo);
    }

}
