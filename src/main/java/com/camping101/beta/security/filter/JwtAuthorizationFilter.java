package com.camping101.beta.security.filter;

import com.amazonaws.util.StringUtils;
import com.camping101.beta.member.exception.ErrorResponse;
import com.camping101.beta.security.JwtProvider;
import com.camping101.beta.security.authentication.UsernamePasswordAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final String ignoreAllPathsStartWith;
    private final String ignoreGetPathsStartWith;
    private Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("======= JwtAuthorizationFilter =======");

        String servletPath = request.getServletPath();
        String authorizationHeader = request.getHeader("Authorization");

        logger.info("요청 경로 : " + servletPath);
        logger.info("Authorization : " + authorizationHeader);

        if (validateIfPathDoesNotNeedAuthentication(servletPath, request.getMethod())) {

            logger.info("JwtAuthorizationFilter : 토큰 확인이 불필요한 경우에 해당됩니다. " +
                    "- 회원가입, 로그인, 토큰 갱신 또는 캠핑장/사이트/캠프로그 정보 조회의 경우");

            filterChain.doFilter(request, response);

        } else if (StringUtils.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {

            logger.info("JwtAuthorizationFilter : 헤더에 토큰 값이 없거나 Bearer로 시작하지 않습니다. => 시큐러티로 PASS");

            filterChain.doFilter(request, response);

        } else {

            // JWT 토큰에서 Access Token을 가져온다.
            String accessToken = authorizationHeader.substring(7);

            // access token 검증
            try {

                Claims claims = jwtProvider.getClaim(accessToken);
                String email = jwtProvider.getEmail(claims);
                List<GrantedAuthority> roles = jwtProvider.getMemberType(claims).stream()
                        .map(x -> "ROLE_" + x)
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

            } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
                response.setStatus(SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ErrorResponse errorResponse = new ErrorResponse("400", e.getMessage());
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
            }
        }
    }

    // 로그인 인증이 불필요한 경우 - 회원가입, 로그인, 토큰 갱신, 또는 캠핑장/사이트/캠프로그 정보 조회
    private boolean validateIfPathDoesNotNeedAuthentication(String servletPath, String httpMethod) {
        if (isInIgnoreAllPathsStartWith(servletPath)) return true;
        else if (isGetMethodAndInIgnoreGetAllPathsStartWith(servletPath, httpMethod)) return true;
        return false;
    }

    private boolean isInIgnoreAllPathsStartWith(String servletPath){
        String[] ignoreAllPathsStartWithArr = ignoreAllPathsStartWith.split(",");
        return Arrays.stream(ignoreAllPathsStartWithArr).filter(x -> servletPath.startsWith(x)).count() >= 1;
    }

    private boolean isGetMethodAndInIgnoreGetAllPathsStartWith(String servletPath, String httpMethod) {
        String[] ignoreGetPathsStartWithArr = ignoreGetPathsStartWith.split(",");
        if ("GET".equals(httpMethod) == false) return false;
        return Arrays.stream(ignoreGetPathsStartWithArr).filter(x -> servletPath.startsWith(x)).count() >= 1;
    }

}
