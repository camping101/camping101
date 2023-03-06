package com.camping101.beta.security.filter;

import com.camping101.beta.member.exception.ErrorResponse;
import com.camping101.beta.security.JwtProvider;
import com.camping101.beta.security.authentication.UsernamePasswordAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("======= JwtAuthorizationFilter =======");

        String authorizationHeader = request.getHeader("Authorization");

        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {

            // 토큰 값이 없거나 Bearer로 시작하지 않는 경우
            logger.info("JwtAuthorizationFilter : 헤더에 토큰 값이 없습니다. ");

            filterChain.doFilter(request, response);

        } else {

            // JWT 토큰에서 Access Token을 가져온다.
            String accessToken = authorizationHeader.substring(7);

            // access token 검증
            try {

                var claims = jwtProvider.getClaim(accessToken);
                var email = jwtProvider.getEmail(claims);
                var roles = jwtProvider.getMemberType(claims).stream()
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                UsernamePasswordAuthentication authentication = new UsernamePasswordAuthentication(email, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Access Token 검증 완료, 인증 유지");

                filterChain.doFilter(request, response);

            } catch(ExpiredJwtException e){

                logger.info("JwtAuthorizationFilter : ACCESS TOKEN이 만료되었습니다.");
                response.setStatus(SC_UNAUTHORIZED);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ErrorResponse errorResponse = new ErrorResponse("401", "Access Token이 만료되었습니다.");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);

            } catch (Exception e) {
                logger.info("JwtAuthorizationFilter : JWT 토큰이 잘못되었습니다. " + e.getMessage());
                response.setStatus(SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ErrorResponse errorResponse = new ErrorResponse("400", "잘못된 JWT Token 입니다.");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
            }
        }
    }

}
