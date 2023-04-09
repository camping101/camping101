package com.camping101.beta.web.domain.member.service.oAuth;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.util.JsonParser;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.db.entity.token.RefreshToken;
import com.camping101.beta.web.domain.member.exception.TokenException;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import static com.camping101.beta.global.config.GoogleOAuthConfig.*;
import static com.camping101.beta.web.domain.member.exception.ErrorCode.INVALID_REFRESH_TOKEN;

/**
 * 구글 OAuth
 * - 구글의 경우 엑세스 토큰(AT)을 리플레시 토큰(RT)로도 사용함
 * - 구글의 엑세스 토큰은 유효 시간이 정해지지 않음.
 * - 인증 Process :
 *   구글 로그인 -> 구글 AT 발급 -> 회원 생성 및 수정 -> 서버 AT, RT 발급 -> 레디스에 구글 AT와 함께 RefreshToken 저장
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService implements OAuthService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final TokenService tokenService;
    private final JsonParser jsonParser;

    @Override
    @Transactional(rollbackOn = {MemberException.class})
    public TokenInfo signInByOAuth(String code){

        GoogleTokenInfo googleTokenInfo = getGoogleTokenInfo(code);
        GoogleAccountInfo googleAccountInfo = parseIdTokenToGoogleAccountInfo(googleTokenInfo.getIdToken());

        Member member = createOrUpdateMember(googleAccountInfo);
        String accessToken = tokenService.createAccessToken(member);
        String refreshToken = tokenService.createRefreshToken(googleTokenInfo.getRefreshToken(), member);

        return new TokenInfo(accessToken, refreshToken);

    }

    // 구글 토큰 정보 조회 - 인증 코드를 통해 AccessToken, RefreshToken, idToken 발급
    private GoogleTokenInfo getGoogleTokenInfo(String code) throws MemberException{

        try {

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            ResponseEntity<String> response = restTemplate.postForEntity(baseUriOf(googleTokenUri), tokenRequest(code), String.class);

            log.info("GoogleOAuthService.getGoogleTokenInfo : {}", response.getBody());

            return jsonParser.parseJsonToObject(response.getBody(), GoogleTokenInfo.class);

        } catch (JsonProcessingException e) {

            log.warn("GoogleOAuthService.exchangeAuthCodeForToken : Json parsing trouble");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);

        } catch (Exception e) {

            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);

        }

    }

    private HttpEntity<MultiValueMap<String, String>> tokenRequest(String code){
        HttpHeaders headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = tokenRequestFormData(code);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> tokenRequestFormData(String code){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        formData.add("grant_type", "authorization_code");
        formData.add("access_type", "offline");
        formData.add("prompt", "content");
        formData.add("code", code);
        return formData;
    }

    // 구글 회원 정보 가져오기 - idToken(JWT)의 payload를 파싱
    private GoogleAccountInfo parseIdTokenToGoogleAccountInfo(String idToken) {

        try {

            return jsonParser.parseJsonPayloadToObject(idToken, GoogleAccountInfo.class);

        } catch (JsonProcessingException e) {

            log.info("GoogleOAuthService.parseIdTokenGoogleAccountInfo : idToken의 payload를 json 파싱 중 오류 발생");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);

        } catch (Exception e) {

            log.info("GoogleOAuthService.parseIdTokenGoogleAccountInfo : idToken값이 잘못된 것 같습니다. 요청 정보를 다시 확인해주세요.");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);

        }
    }

    private Member createOrUpdateMember(GoogleAccountInfo googleAccountInfo) {

        Optional<Member> optionalMember = memberRepository.findByEmail(googleAccountInfo.getEmail());

        if (optionalMember.isPresent()) {
            // 이미 가입한 경우 회원 프로필, 닉네임 업데이트
            Member member = optionalMember.get();
            member.setProfileImagePath(googleAccountInfo.getPicture());
            member.setNickname(googleAccountInfo.getName());
            return memberRepository.save(member);
        }

        return memberRepository.save(googleAccountInfo.toActivatedMember());
    }

    // 서버 AT 토큰 갱신
    @Override
    public TokenInfo reissueAccessTokenByRefreshToken(String refreshToken) throws TokenException{

        RefreshToken rt = tokenService.findRefreshTokenFromRedis(refreshToken)
                .orElseThrow(() -> new TokenException(INVALID_REFRESH_TOKEN));



        GoogleTokenInfo newGoogleTokenInfo = reissueGoogleAccessToken(rt.getGoogleRefreshToken());
        GoogleAccountInfo newGoogleAccountInfo = parseIdTokenToGoogleAccountInfo(newGoogleTokenInfo.getIdToken());

        Member member = createOrUpdateMember(newGoogleAccountInfo);
        String newAccessToken = tokenService.createAccessToken(member);
        // TODO 여기부터
        String newRefreshToken = tokenService.createRefreshToken(newGoogleTokenInfo.getAccessToken(), member);

        return new TokenInfo(newAccessToken, newRefreshToken);
        
    }

    // 구글 RT 토큰 갱신 - 실제로는 AT(Access Token)이나, 구글의 경우 RT(Refresh Token)로도 사용됨
    private GoogleTokenInfo reissueGoogleAccessToken(String googleAccessToken){

        try {

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            URI uri = baseUriOf(googleTokenUri);
            HttpEntity<MultiValueMap<String, String>> request = postRefreshTokenRequest(googleAccessToken);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

            log.info("Google Response : \n" + response.getBody());

            return jsonParser.parseJsonToObject(response.getBody(), GoogleTokenInfo.class);

        } catch (JsonProcessingException e) {
            log.warn("GoogleOAuthService.refreshGoogleToken : Json parsing trouble");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        } catch (Exception e) {
            log.warn("GoogleOAuthService.refreshGoogleToken : Refresh Token을 전송하는 과정에서 무엇인가 잘못되었습니다!");
            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
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
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);
        formData.add("grant_type", "refresh_token");
        return formData;
    }

    // 탈퇴 또는 로그아웃시 revoke 처리
    @Override
    public void revokeAccessTokenForLogOut(String serverAccessToken) {

        try {
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            URI uri = baseUriOf(googleRevokeUri);
            String googleAccessToken = "tokenService."; // TODO
            HttpEntity<MultiValueMap<String, String>> request = postRevokeRequest(googleAccessToken);
            restTemplate.postForEntity(uri, request, String.class);
        } catch(MemberException e) {
            log.info("구글 로그아웃 중 이슈 : " + e.getMessage());
        } catch (Exception e) {
            log.warn("구글 로그아웃 중 이슈 : " + e.getMessage());
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



}
