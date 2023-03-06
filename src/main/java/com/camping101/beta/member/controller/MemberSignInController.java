package com.camping101.beta.member.controller;

import com.camping101.beta.member.dto.MemberSignInRequest;
import com.camping101.beta.member.dto.MemberSignInResponse;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signin")
@Api(tags = {"로그인 API"})
public class MemberSignInController {

    private final MemberSignInService memberSignInService;
    private final OAuthService googleOAuthService;

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
    public ResponseEntity<MemberSignInResponse> createOrUpdateMemberWhenSignIn(
            @RequestParam String code, @ApiIgnore HttpServletResponse response) {

        var memberInfo = googleOAuthService.createOrUpdateMemberWhenSignIn(code);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("ACCESS_TOKEN", memberInfo.getAccessToken());
        response.setHeader("REFRESH_TOKEN",memberInfo.getRefreshToken());

        return ResponseEntity.ok(memberInfo);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(Map<String, String> refreshMap,
                                          @ApiIgnore HttpServletRequest request,
                                          @ApiIgnore HttpServletResponse response){

        var authorizationHeader = request.getHeader("Authorization");
        var serverRefreshToken = refreshMap.get("REFRESH_TOKEN");

        if (StringUtils.isNullOrEmpty(authorizationHeader)
            || !authorizationHeader.startsWith("Basic")
            || StringUtils.isNullOrEmpty(serverRefreshToken)) {
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }

        var memberInfo = googleOAuthService.renewToken(authorizationHeader.substring(6), serverRefreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("ACCESS_TOKEN", memberInfo.getAccessToken());
        response.setHeader("REFRESH_TOKEN", memberInfo.getRefreshToken());

        return ResponseEntity.ok().build();
    }

}
