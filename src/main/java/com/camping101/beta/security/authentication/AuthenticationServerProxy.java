package com.camping101.beta.security.authentication;

import com.camping101.beta.member.dto.MemberSignInRequest;
import com.camping101.beta.member.entity.type.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class AuthenticationServerProxy {

    private final RestTemplate restTemplate;
    @Value("${server.base.url}")
    private String baseUrl;

    public boolean sendSignInByMailRequest(String email, String password, List<String> roles) {

        String url = baseUrl + "/api/signin/mail";

        MemberSignInRequest memberSignInRequest = new MemberSignInRequest();
        memberSignInRequest.setEmail(email);
        memberSignInRequest.setPassword(password);

        if (!CollectionUtils.isEmpty(roles)) {
            memberSignInRequest.setMemberType(MemberType.valueOf(roles.get(0).toUpperCase(Locale.ROOT)));
        }

        HttpEntity request = new HttpEntity<>(memberSignInRequest);
        ResponseEntity response = restTemplate.postForEntity(url, request, Void.class);

        return response.getStatusCode().equals(HttpStatus.OK);
    }

}
