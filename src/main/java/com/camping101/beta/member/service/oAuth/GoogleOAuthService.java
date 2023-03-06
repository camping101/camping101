package com.camping101.beta.member.service.oAuth;

import com.camping101.beta.member.dto.MemberSignInResponse;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.type.SignUpType;
import com.camping101.beta.member.exception.ErrorCode;
import com.camping101.beta.member.exception.MemberException;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.member.service.MemberSignInServiceImpl;
import com.camping101.beta.security.JwtProvider;
import com.camping101.beta.util.RedisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RedisClient redisClient;
    private final MemberRepository memberRepository;
    private final Logger logger = Logger.getLogger(MemberSignInServiceImpl.class.getName());

    // 서버용 Access Token 만료 시간
    @Value("${token.jwt.accesstoken}")
    private long ACCESS_EXPIRE_MILLISECOND; // 12시간

    // 구글 Cloud api 보안 정보
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    // 구글 토큰 서버 (엑세스 토큰 반환)
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String GOOGLE_TOKEN_SERVER_URI;

    // 구글 사용자 API 서버
    private final String USER_INFO_BASE_URI = "https://www.googleapis.com/oauth2/v2/userinfo";

    // 구글 인증 API - revoke uri (로그아웃 용도)
    private final String AUTHORIZATION_REVOKE_URI = "https://accounts.google.com/o/oauth2/revoke";
    private final JwtProvider jwtProvider;

    // Flow :
    // 1. 사용자 확인       : /api/signin/google --> 구글 인증 서버 호출 : 사용자 확인 --> api/signin/oauth/google (redirect)
    // 2. 토큰 발급         : 1.에서 받은 code로 토큰 발급 요청 --> Redis에 토큰 정보 저장 * key=encoded access token, value=google token 정보
    // 3. 사용자 로그인 유지 : JWT 토큰의 encodedAccessToken이 유효할 경우 로그인 상태 유지
    // 4. 로그아웃(탈퇴 시 자동 로그아웃) 시에 Access Token revoke 요청
    @Override
    @Transactional(rollbackOn = {MemberException.class})
    public MemberSignInResponse createOrUpdateMemberWhenSignIn(String code){

        var googleTokenInfo = exchangeCodeForToken(code);
        var googleAccountInfo = exchangeAccessTokenForAccountInfo(googleTokenInfo);
        var member = createOrUpdateMember(googleAccountInfo);
        var serverAccessToken = createJwtToken(member, ACCESS_EXPIRE_MILLISECOND);
        var serverRefreshToken = createJwtToken(member, Long.MAX_VALUE); // never expire
        saveGoogleTokenInfoIntoRedis(serverAccessToken, googleTokenInfo);
        return new MemberSignInResponse("Bearer " + serverAccessToken, "Basic " + serverRefreshToken);

    }

    private String createJwtToken(Member member, long expiredSecond) {
        logger.info("GoogleOAuthService의 createJwtToken: expiredMilliSecond는 " + expiredSecond);
        return jwtProvider.createToken(
                member.getEmail(), List.of(member.getMemberType().getAuthority()), expiredSecond);
    }

    // 토큰 발급
    private GoogleTokenInfo exchangeCodeForToken(String code) throws MemberException{

        logger.info("Just Sent Authorization Code To Google Token Server!");

        try {

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            var uri = baseUriOf(GOOGLE_TOKEN_SERVER_URI);
            var request = postCodeForTokenRequest(code);
            var response = restTemplate.postForEntity(uri, request, String.class);
            var token = parseJsonToGoogleToken(response.getBody());

            logger.info("Google Response : \n" + response.getBody());

            return token;

        } catch (JsonProcessingException e) {
            logger.warning("Json parsing trouble");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.warning(x.toString()));
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }

    }

    private HttpEntity<?> postCodeForTokenRequest(String code){
        var headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        var body = exchangeCodeForTokenFormData(code);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> exchangeCodeForTokenFormData(String code){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", CLIENT_ID);
        formData.add("client_secret", CLIENT_SECRET);
        formData.add("redirect_uri", REDIRECT_URI);
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        return formData;
    }

    private GoogleTokenInfo parseJsonToGoogleToken(String jsonBody) throws JsonProcessingException {
        var token = parseJsonToObject(jsonBody, GoogleTokenInfo.class);
        token.setExpiredAt(LocalDateTime.now().plusSeconds(token.getExpiresIn()));
        token.setRefreshToken(token.getIdToken());
        return token;
    }

    // 사용자 정보 요청
    private GoogleAccountInfo exchangeAccessTokenForAccountInfo(GoogleTokenInfo token) throws MemberException{

        logger.info("Just Sent Access Token!");

        try {

            var uri = baseUriOf(USER_INFO_BASE_URI);
            var request = getAccountInfoRequest(token.getAccessToken());
            var response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);

            logger.info("Member Info : \n" + response.getBody());

            return parseJsonToObject(response.getBody(), GoogleAccountInfo.class);

        } catch (JsonProcessingException e) {
            logger.warning("Json parsing trouble");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.warning(x.toString()));
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }
    }

    private HttpEntity<?> getAccountInfoRequest(String accessToken) {
        var headers = requestHeadersOf(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }

    private Member createOrUpdateMember(GoogleAccountInfo googleAccountInfo) {
        var optionalMember = memberRepository.findByEmail(googleAccountInfo.getEmail());
        if (optionalMember.isPresent()) {
            logger.info("This member already signed up by google account before, " +
                    "and we don't change member info by its google account as it is.");
            return optionalMember.get();
        }
        return memberRepository.save(Member.from(googleAccountInfo));
    }

    private void saveGoogleTokenInfoIntoRedis(String serverAccessToken, GoogleTokenInfo tokenInfo) throws MemberException{
        try {
            redisClient.put(serverAccessToken, tokenInfo);
        } catch (Exception e) {
            logger.warning("Redis Connection should be checked!!");
            logger.warning(e.getMessage());
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }
    }
    
    // access token 만료 시 refresh token으로 새로 발급
    @Override
    public MemberSignInResponse renewToken(String previousServerAccessToken, String previousRefreshToken) {

        logger.info("Just Sent refresh Token To Google Token Server!");
        
        try {

            var tokenInfo = getTokenInfoFromRedis(previousServerAccessToken);
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            var uri = baseUriOf(GOOGLE_TOKEN_SERVER_URI);
            var request = postAccessTokenRenewRequest(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
            var response = restTemplate.postForEntity(uri, request, String.class);

            logger.info("Google Response : \n" + response.getBody());

            var newTokenInfo = parseJsonToGoogleToken(response.getBody());
            var memberInfo = exchangeAccessTokenForAccountInfo(newTokenInfo);
            var member = createOrUpdateMember(memberInfo);
            var currentServerAccessToken = createJwtToken(member, tokenInfo.getExpiresIn());
            var currentServerRefreshToken = createJwtToken(member, Long.MAX_VALUE); // never expire
            updateAccessTokenInRedis(previousServerAccessToken, currentServerAccessToken, newTokenInfo);

            return new MemberSignInResponse(currentServerAccessToken, currentServerRefreshToken);

        } catch (JsonProcessingException e) {
            logger.warning("Json parsing trouble");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.warning(x.toString()));
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }
        
    }

    private void updateAccessTokenInRedis(String previousServerAccessToken,
                                          String currentServerAccessToken,
                                          GoogleTokenInfo newToken) {
        redisClient.delete(previousServerAccessToken);
        redisClient.put(currentServerAccessToken, newToken);
    }

    private GoogleTokenInfo getTokenInfoFromRedis(String serverAccessToken) throws MemberException{
        var optionalTokenInfo = redisClient.get(serverAccessToken, GoogleTokenInfo.class);
        if (optionalTokenInfo.isEmpty()){
            logger.warning("GoogleOAuthService : Token 정보를 찾을 수 없습니다.");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }
        return optionalTokenInfo.get();
    }

    private HttpEntity<?> postAccessTokenRenewRequest(String accessToken, String refreshToken){
        var headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + accessToken);
        var body = bodyOfExchangeRefreshTokenForAccessTokenRequest(refreshToken);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> bodyOfExchangeRefreshTokenForAccessTokenRequest(String refreshToken){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);
        return formData;
    }

    private URI baseUriOf(String USER_INFO_BASE_URI) {
        return UriComponentsBuilder
                .fromUriString(USER_INFO_BASE_URI)
                .encode()
                .build().toUri();
    }

    private HttpHeaders requestHeadersOf(MediaType mediaType) {
        var headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return headers;
    }

    private <T> T parseJsonToObject(String jsonResponse, Class<T> classType) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, classType);
    }

    // 탈퇴 또는 로그아웃시 revoke 처리
    @Override
    public void revokeAccessTokenForLogOut(String serverAccessToken) {

        try {
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            var uri = baseUriOf(AUTHORIZATION_REVOKE_URI);
            var googleTokenInfo = getTokenInfoFromRedis(serverAccessToken);
            var request = postRevokeRequest(googleTokenInfo.getAccessToken());
            var response = restTemplate.postForEntity(uri, request, String.class);
        } catch(MemberException e) {
            logger.info("구글 로그아웃 중 이슈 : " + e.getMessage());
        } catch (Exception e) {
            logger.warning("구글 로그아웃 중 이슈 : " + e.getMessage());
        }

    }

    private HttpEntity<?> postRevokeRequest(String accessToken){
        var headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        var body = bodyOfRevokeRequest(accessToken);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> bodyOfRevokeRequest(String accessToken){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", accessToken);
        return formData;
    }


}
