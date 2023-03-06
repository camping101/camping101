package com.camping101.beta.security.filter;

import com.camping101.beta.security.JwtProvider;
import com.camping101.beta.security.authentication.UsernamePasswordAuthentication;
import com.camping101.beta.util.RedisClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RedisClient redisClient;
    @Value("${token.jwt.accesstoken}")
    private long ACCESS_EXPIRE_MILLISECOND; // 12시간
    @Value("${token.jwt.refreshtoken}")
    private long REFRESH_EXPIRE_MILLISECOND; // 3개월
    private Logger logger = Logger.getLogger(JwtAuthorizationFilter.class.getName());

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        logger.info("======= JwtAuthenticationFilter =======");

        var email = request.getParameter("email");
        var password = request.getParameter("password");
        var memberType = request.getParameter("memberType");
        var roles = List.of(memberType).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        var authentication = new UsernamePasswordAuthentication(email, password, roles);

        logger.info(String.format("SignIn Attempt --> id : %s, pw : %s\n", email, password));

        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        logger.info("인증에 성공하였습니다.");

        var user = (User) authentication.getPrincipal();
        var roles = user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList());

        String accessToken = jwtProvider.createToken(user.getUsername(), roles, ACCESS_EXPIRE_MILLISECOND);
        String refreshToken = jwtProvider.createToken(user.getUsername(), roles, REFRESH_EXPIRE_MILLISECOND);

        // RefreshToken을 Redis에 저장
        redisClient.put(user.getUsername(), refreshToken);

        // Access Token, Refresh Token을 프론트 단에 Response Header로 전달.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("ACCESS_TOKEN", accessToken);
        response.setHeader("REFRESH_TOKEN", refreshToken);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("ACCESS_TOKEN", accessToken);
        responseMap.put("REFRESH_TOKEN", refreshToken);
        new ObjectMapper().writeValue(response.getWriter(), responseMap);

    }

}
