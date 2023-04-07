package com.camping101.beta.global.security;

import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.global.security.authentication.UsernamePasswordAuthenticationProvider;
import com.camping101.beta.global.security.filter.JwtAuthenticationFilter;
import com.camping101.beta.global.security.filter.JwtAuthorizationFilter;
import com.camping101.beta.util.RedisClient;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.service.oAuth.OAuthService;
import com.camping101.beta.web.domain.token.service.TokenService;
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
    private final OAuthService googleOAuthService;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

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

        http
                .cors().and()
                .csrf().disable()
                .addFilterAt(new JwtAuthenticationFilter(authenticationManager(), tokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(tokenService, ignoreAllPathsStartWith, ignoreGetPathsStartWith), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/h2-console/**", "/swagger-ui.html", "/v2/api-docs")
                .permitAll()

                .antMatchers(ignoreAllPathsStartWith.split(","))
                .permitAll()
                .antMatchers(HttpMethod.GET, ignoreGetPathsStartWith.split(","))
                .permitAll()

                .antMatchers("/api/member","/api/member/**")
                .hasRole(MemberType.CUSTOMER.name())

                .antMatchers("/api/admin/**")
                .hasRole(MemberType.ADMIN.name()).and()

                .formLogin().loginProcessingUrl("/api/signin").and()
                .logout().logoutUrl("/api/signout")
                .addLogoutHandler(new MemberSignOutHandler(googleOAuthService, memberRepository, tokenService));
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

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

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
