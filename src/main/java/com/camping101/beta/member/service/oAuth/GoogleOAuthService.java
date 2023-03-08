package com.camping101.beta.member.service.oAuth;

import com.camping101.beta.member.dto.TokenInfo;
import com.camping101.beta.member.entity.Member;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RedisClient redisClient;
    private final MemberRepository memberRepository;
    private final Logger logger = Logger.getLogger(MemberSignInServiceImpl.class.getName());

    // 구글 API 사용 위한 보안 정보
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    // 구글 토큰 서버 API (엑세스 토큰을 얻거나 갱신)
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String GOOGLE_TOKEN_URI;

    // 구글 idToken JWT 파싱 key
    @Value("${oauth2.client.provider.google.jwt-key}")
    private String GOOGLE_JWT_PARSE_KEY;

    // 구글 인증 서버의 Revoke URI (로그아웃 용도)
    @Value("${oauth2.client.provider.google.all-token-revoke-uri}")
    private String GOOGLE_AUTHORIZATION_REVOKE_URI;

    @Value("${token.jwt.refreshtoken}")
    private long refreshTokenExpirationTime;
    
    private final JwtProvider jwtProvider;

    // Flow : 
    // 사용자 확인 후 신규 토큰 발급 -> 갱신 토큰으로 인증 상태 유지 -> 탈퇴 및 로그아웃
    // * 구글 인증 세션 : 1시간 단위
    @Override
    @Transactional(rollbackOn = {MemberException.class})
    public TokenInfo signInByOAuth(String code){

        GoogleTokenInfo googleTokenInfo = exchangeCodeForToken(code);
        GoogleAccountInfo googleAccountInfo = parseIdTokenToGoogleAccountInfo(googleTokenInfo.getIdToken());
        Member member = createOrUpdateMember(googleAccountInfo);
        String serverAccessToken = createJwtToken(member, 1000 * googleTokenInfo.getExpiresIn()); // 1시간 (3600초)
        String serverRefreshToken = createJwtToken(member, refreshTokenExpirationTime);
        saveGoogleAccessTokenIntoRedis(serverAccessToken, googleTokenInfo.getAccessToken());
        return new TokenInfo("Bearer " + serverAccessToken, "Basic " + serverRefreshToken);

    }

    // 토큰 발급
    private GoogleTokenInfo exchangeCodeForToken(String code) throws MemberException{

        try {

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            URI uri = baseUriOf(GOOGLE_TOKEN_URI);
            HttpEntity<MultiValueMap<String, String>> request = postCodeForTokenRequest(code);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
            GoogleTokenInfo token = parseJsonToObject(response.getBody(), GoogleTokenInfo.class);

            logger.info("Google Token Info : \n" + response.getBody());

            return token;

        } catch (JsonProcessingException e) {
            logger.warning("Json parsing trouble");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.warning(x.toString()));
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }

    }

    private HttpEntity<MultiValueMap<String, String>> postCodeForTokenRequest(String code){
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

    // 회원 정보 가져오기 - idToken(JWT)를 파싱
    private GoogleAccountInfo parseIdTokenToGoogleAccountInfo(String idToken) {
        try {

            String payload = idToken.split("\\.")[1];
            String decodedPayload = new String(Base64Utils.decodeFromUrlSafeString(payload));
            GoogleAccountInfo googleAccountInfo = parseJsonToObject(decodedPayload, GoogleAccountInfo.class);

            logger.info("사용자 정보 파싱 >>> " + googleAccountInfo.getEmail());

            return googleAccountInfo;

        } catch (JsonProcessingException e) {
            logger.info("GoogleOAuthService.parseIdTokenGoogleAccountInfo : idToken의 payload를 json 파싱 중 오류 발생");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        } catch (Exception e) {
            logger.info("GoogleOAuthService.parseIdTokenGoogleAccountInfo : idToken값이 잘못된 것 같습니다. 요청 정보를 다시 확인해주세요.");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }
    }

    private String createJwtToken(Member member, long expiredSecond) {
        logger.info("GoogleOAuthService의 createJwtToken: expiredMilliSecond는 " + expiredSecond);
        return jwtProvider.createToken(
                member.getEmail(), List.of(member.getMemberType().name()), expiredSecond);
    }

    private void saveGoogleAccessTokenIntoRedis(String serverAccessToken, String googleAccessToken) throws MemberException{
        try {
            redisClient.put(serverAccessToken, googleAccessToken);
            logger.info("Redis에 저장이 잘 되었습니다!");
        } catch (Exception e) {
            logger.warning("Redis Connection should be checked!!");
            logger.warning(e.getMessage());
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }
    }

    private Member createOrUpdateMember(GoogleAccountInfo googleAccountInfo) {
        Optional<Member> optionalMember = memberRepository.findByEmail(googleAccountInfo.getEmail());
        if (optionalMember.isPresent()) {
            logger.info("이미 가입을 했기 때문에 회원 정보를 수정합니다.");
            Member member = optionalMember.get();
            member.changeImage(googleAccountInfo.getPicture());
            member.changeNickname(googleAccountInfo.getName());
            return memberRepository.save(member);
        }
        logger.info("새롭게 회원가입 처리를 합니다.");
        return memberRepository.save(Member.from(googleAccountInfo));
    }

    
    // access token 만료 시 refresh token으로 새로 발급
    @Override
    public TokenInfo renewToken(String previousServerAccessToken, String previousRefreshToken) {

        String googleAccessToken = getGoogleAccessTokenFromRedis(previousServerAccessToken);
        GoogleTokenInfo newGoogleTokenInfo = exchangeGoogleAccessTokenForNewTokenInfo(googleAccessToken);
        GoogleAccountInfo newGoogleAccountInfo = parseIdTokenToGoogleAccountInfo(newGoogleTokenInfo.getIdToken());
        Member member = createOrUpdateMember(newGoogleAccountInfo);
        String currentServerAccessToken = createJwtToken(member, 1000 * newGoogleTokenInfo.getExpiresIn());
        String currentServerRefreshToken = createJwtToken(member, refreshTokenExpirationTime);
        updateGoogleAccessTokenInRedis(previousServerAccessToken, currentServerAccessToken, newGoogleTokenInfo.getAccessToken());

        return new TokenInfo("Bearer " + currentServerAccessToken, "Basic " + currentServerRefreshToken);
        
    }

    private String getGoogleAccessTokenFromRedis(String previousServerAccessToken) throws MemberException{
        Optional<String> optionalGoogleAccessToken
                = redisClient.get(previousServerAccessToken.substring(7), String.class);
        if (optionalGoogleAccessToken.isEmpty()){
            logger.warning("GoogleOAuthService.getTokenInfoFromRedis : Refresh Token 정보를 Redis에서 찾을 수 없습니다.");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }
        return optionalGoogleAccessToken.get();
    }

    private GoogleTokenInfo exchangeGoogleAccessTokenForNewTokenInfo(String googleAccessToken){

        try {

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            URI uri = baseUriOf(GOOGLE_TOKEN_URI);
            HttpEntity<MultiValueMap<String, String>> request = postRefreshTokenRequest(googleAccessToken);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

            logger.info("Google Response : \n" + response.getBody());

            return parseJsonToObject(response.getBody(), GoogleTokenInfo.class);

        } catch (JsonProcessingException e) {
            logger.warning("GoogleOAuthService.refreshGoogleToken : Json parsing trouble");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        } catch (Exception e) {
            logger.warning("GoogleOAuthService.refreshGoogleToken : Refresh Token을 전송하는 과정에서 무엇인가 잘못되었습니다!");
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.warning(x.toString()));
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }

    }

    private HttpEntity<MultiValueMap<String, String>> postRefreshTokenRequest(String googleAccessToken){
        HttpHeaders headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = bodyOfRefreshTokenFormData(googleAccessToken);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> bodyOfRefreshTokenFormData(String refreshToken){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", CLIENT_ID);
        formData.add("client_secret", CLIENT_SECRET);
        formData.add("refresh_token", refreshToken);
        formData.add("grant_type", "refresh_token");
        return formData;
    }

    private void updateGoogleAccessTokenInRedis(String previousServerAccessToken,
                                                String currentServerAccessToken,
                                                String currentGoogleAccessToken) {
        redisClient.delete(previousServerAccessToken);
        redisClient.put(currentServerAccessToken, currentGoogleAccessToken);
    }

    // 탈퇴 또는 로그아웃시 revoke 처리
    @Override
    public void revokeAccessTokenForLogOut(String serverAccessToken) {

        try {
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            URI uri = baseUriOf(GOOGLE_AUTHORIZATION_REVOKE_URI);
            String googleAccessToken = getGoogleAccessTokenFromRedis(serverAccessToken);
            HttpEntity<MultiValueMap<String, String>> request = postRevokeRequest(googleAccessToken);
            restTemplate.postForEntity(uri, request, String.class);
        } catch(MemberException e) {
            logger.info("구글 로그아웃 중 이슈 : " + e.getMessage());
        } catch (Exception e) {
            logger.warning("구글 로그아웃 중 이슈 : " + e.getMessage());
        }

    }

    private HttpEntity<MultiValueMap<String, String>> postRevokeRequest(String googleAccessToken){
        HttpHeaders headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = bodyOfRevokeRequest(googleAccessToken);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> bodyOfRevokeRequest(String googleAccessToken){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", googleAccessToken);
        return formData;
    }

    // 보조 메소드들
    private URI baseUriOf(String USER_INFO_BASE_URI) {
        return UriComponentsBuilder
                .fromUriString(USER_INFO_BASE_URI)
                .encode()
                .build().toUri();
    }

    private HttpHeaders requestHeadersOf(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return headers;
    }

    private <T> T parseJsonToObject(String jsonResponse, Class<T> classType) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, classType);
    }

}
