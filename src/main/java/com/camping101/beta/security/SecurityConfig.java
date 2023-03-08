package com.camping101.beta.security;

import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.member.service.oAuth.OAuthService;
import com.camping101.beta.security.authentication.UsernamePasswordAuthenticationProvider;
import com.camping101.beta.security.filter.JwtAuthenticationFilter;
import com.camping101.beta.security.filter.JwtAuthorizationFilter;
import com.camping101.beta.util.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    private final JwtProvider jwtProvider;
    private final RedisClient redisClient;
    private final OAuthService googleOAuthService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${security.ignore.all.paths.startwith}")
    private String ignoreAllPathsStartWith;
    @Value("${security.ignore.get.paths.startwith}")
    private String ignoreGetPathsStartWith;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(usernamePasswordAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                // Jwt filter
                .addFilterAt(
                        new JwtAuthenticationFilter(authenticationManager(), jwtProvider, redisClient),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        new JwtAuthorizationFilter(jwtProvider, ignoreAllPathsStartWith, ignoreGetPathsStartWith),
                        UsernamePasswordAuthenticationFilter.class)
                // Path 권한
                .authorizeRequests()
                // - H2 및 Swagger 사용 허용
                .antMatchers("/h2-console/**", "/swagger-ui.html", "/v2/api-docs")
                .permitAll()
                // - 비회원
                .antMatchers(ignoreAllPathsStartWith.split(","))
                .permitAll()
                .antMatchers(HttpMethod.GET, ignoreGetPathsStartWith.split(","))
                .permitAll()
                // - 일반 회원 (CUSTOMER)
                .antMatchers("/api/member","/api/member/**")
                .hasRole(MemberType.CUSTOMER.name())
                // - 캠핑장 주인 (OWNER)
                // TODO 작성 필요
                // - 관리자 (ADMIN)
                .antMatchers("/api/admin/**")
                .hasRole(MemberType.ADMIN.name())
                .and()
                // 로그인
                .formLogin()
                .loginProcessingUrl("/api/signin")
                .and()
                .oauth2Login()
                .and()
                // 로그아웃
                .logout()
                .logoutUrl("/api/signout")
                .addLogoutHandler(new MemberSignOutHandler(googleOAuthService, memberRepository, jwtProvider));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "/swagger-ui.html/**","/swagger-ui.html/#/**", "/v2/api-docs")
                .antMatchers("/css/**", "/vendor/**", "/js/**", "/images/**");
        web.ignoring()
                .mvcMatchers("/swagger-ui/*", "/swagger-resources/**", "/swagger-ui.html");
        web.ignoring()
                .antMatchers(HttpMethod.GET, "/api/camplog","/api/site","/api/camp")
                .antMatchers("/api/signup","/api/signin");
        web.ignoring().antMatchers("*");
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
