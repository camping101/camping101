package com.camping101.beta.security;

import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.service.oAuth.OAuthService;
import com.camping101.beta.security.authentication.UsernamePasswordAuthenticationProvider;
import com.camping101.beta.security.filter.JwtAuthenticationFilter;
import com.camping101.beta.security.filter.JwtAuthorizationFilter;
import com.camping101.beta.util.RedisClient;
import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    private final JwtProvider jwtProvider;
    private final RedisClient redisClient;
    private final OAuthService googleOAuthService;
    private final Logger logger = Logger.getLogger(SecurityConfig.class.getName());

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(usernamePasswordAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();
        http.formLogin()
                .loginProcessingUrl("/api/signin");
        http.oauth2Login();
        http
                .addFilterAt(new JwtAuthenticationFilter(authenticationManager(), jwtProvider, redisClient), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/h2-console/**")
                .permitAll();
        http.authorizeRequests()
                .mvcMatchers("/", "/api/signup/**", "/api/signin/**", "/api/signin/oauth/google")
                .permitAll();
        http.authorizeRequests()
                .mvcMatchers("/api/member","/api/member/*", "/api/member/password")
                .hasRole(MemberType.CUSTOMER.name())
                .mvcMatchers("/api/admin/**")
                .hasRole(MemberType.ADMIN.name());
        http.logout()
                .logoutUrl("/api/logout")
                .addLogoutHandler((request, response, authentication) -> {

                    var authorization = request.getHeader("Authorization");

                    if (!StringUtils.isNullOrEmpty(authorization) && authorization.startsWith("Bearer ")) {
                        googleOAuthService.revokeAccessTokenForLogOut(authorization.substring(7));
                    }

                    HttpSession session = request.getSession();
                    if (Objects.nonNull(session)) {
                        logger.info("All Session Jwt Invalidated");
                        session.invalidate();
                    }
                });
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**")
                .antMatchers("/css/**", "/vendor/**", "/js/**", "/images/**");
        web.ignoring()
                .mvcMatchers("/swagger-ui/*", "/swagger-resources/**", "/swagger-ui.html");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
