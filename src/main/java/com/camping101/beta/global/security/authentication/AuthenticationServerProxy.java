package com.camping101.beta.global.security.authentication;

import com.camping101.beta.web.domain.member.dto.MemberSignInRequest;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.web.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServerProxy {

    private final RestTemplate restTemplate;
    @Value("${server.base.url}")
    private String baseUrl;

    public boolean sendSignInByMailRequest(String email, String password, List<GrantedAuthority> roles) {

        try {

            String url = baseUrl + "/api/signin/mail/authenticate";
            MemberSignInRequest requestDto = signInByEmailRequestDto(email, password, roles);
            HttpEntity request = new HttpEntity<>(requestDto);
            ResponseEntity response = restTemplate.postForEntity(url, request, Void.class);
            return response.getStatusCode().equals(HttpStatus.OK);

        } catch (MemberException e) {

            log.warn("AuthenticationProxy.sendSignInByMailRequest : MemberType이 없습니다.");
            return false;

        }

    }

    private MemberSignInRequest signInByEmailRequestDto(String email, String password,
                                                        List<GrantedAuthority> roles) throws MemberException{
        MemberSignInRequest memberSignInRequest = new MemberSignInRequest();
        memberSignInRequest.setEmail(email);
        memberSignInRequest.setPassword(password);
        memberSignInRequest.setMemberType(MemberType.of(roles));
        return memberSignInRequest;
    }

}
