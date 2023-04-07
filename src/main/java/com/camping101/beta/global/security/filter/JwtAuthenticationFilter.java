package com.camping101.beta.global.security.filter;

import com.camping101.beta.global.security.MemberDetails;
import com.camping101.beta.global.security.authentication.UsernamePasswordAuthentication;
import com.camping101.beta.util.FilterResponseHandler;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        log.info("JwtAuthenticationFilter 접근 경로 : {}", request.getServletPath());

        try {

            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String memberType = request.getParameter("memberType");
            List<GrantedAuthority> roles = List.of(memberType).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(roles)) {
                FilterResponseHandler.sendFilterExceptionResponse(response, "회원 타입이 요청에 없습니다", HttpStatus.BAD_REQUEST.value());
            }

            log.info("JwtAuthenticationFilter.attemptAuthentication : 로그인 시도 >> email : {}, pw : {}", email, password);

            return authenticationManager.authenticate(new UsernamePasswordAuthentication(email, password, roles));

        } catch (IOException e) {

            log.warn("JwtAuthenticationFilter.attemptAuthentication : IOException");
            e.getStackTrace();

            throw new AuthenticationServiceException("IOException 발생", e);

        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {

        log.info("JwtAuthenticationFilter.successAuthentication : 인증 성공. Access Token, Refresh Token 발급.");

        try {

            MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
            String accessToken = tokenService.createAccessToken(memberDetails);
            String refreshToken = tokenService.createRefreshToken(memberDetails);
            addAccessTokenAndRefreshTokenToHeader(response, accessToken, refreshToken);
            FilterResponseHandler.sendSuccessResponse(response, tokenHeader(accessToken, refreshToken));

        } catch (MemberException e) {

            log.warn("JwtAuthenticationFilter.successAuthentication : 회원 타입이 Authentication에 없음");
            FilterResponseHandler.sendFilterExceptionResponse(response, "시큐러티 에러 - 회원 타입이 Authentication에 없음", HttpStatus.INTERNAL_SERVER_ERROR.value());

        }

    }

    private static void addAccessTokenAndRefreshTokenToHeader(HttpServletResponse response,
                                                              String accessToken, String refreshToken) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("ACCESS_TOKEN", accessToken);
        response.setHeader("REFRESH_TOKEN", refreshToken);
    }

    private static Map<String, String> tokenHeader(String accessToken, String refreshToken) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("ACCESS_TOKEN", accessToken);
        responseMap.put("REFRESH_TOKEN", refreshToken);
        return responseMap;
    }

}
