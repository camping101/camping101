package com.camping101.beta.security.authentication;

import com.camping101.beta.member.dto.MemberSignInRequest;
import com.camping101.beta.member.entity.type.MemberType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
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
    private final ObjectMapper objectMapper;
    private final Logger logger = Logger.getLogger(AuthenticationServerProxy.class.getName());

    @Value("${server.base.url}")
    private String baseUrl;

    public boolean sendSignInByMailRequest(String email, String password, List<String> roles) {

        String url = baseUrl + "/api/signin/mail";

        var memberSignInRequest = new MemberSignInRequest();
        memberSignInRequest.setEmail(email);
        memberSignInRequest.setPassword(password);

        if (!CollectionUtils.isEmpty(roles)) {
            memberSignInRequest.setMemberType(MemberType.valueOf(roles.get(0).toUpperCase(Locale.ROOT)));
        }

        var request = new HttpEntity<>(memberSignInRequest);
        var response = restTemplate.postForEntity(url, request, Void.class);

        return response.getStatusCode().equals(HttpStatus.OK);
    }

    public boolean sendSignInByGoogleRequest(){

        // 사용자에게 로그인 및 권한 승인 요청 (인증 코드 반환)
        String url = baseUrl + "/oauth2/authorization/google";
        var code = restTemplate.getForObject(url,String.class);

        logger.info(code);

        // 토큰 발행 후 회원 생성 또는 업데이트
        url = baseUrl + "/api/signin/google?code=" + code;
        Map<String, String> codeMap = new HashMap<>();
        codeMap.put("code", code);
        var request = new HttpEntity<>(codeMap);
        var response = restTemplate.postForEntity(url, request, Void.class);

        return response.getStatusCode().equals(HttpStatus.OK);

    }

}
