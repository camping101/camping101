package com.camping101.beta.global.security;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.service.oAuth.OAuthService;
import com.camping101.beta.web.domain.token.service.TokenService;
import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class MemberSignOutHandler implements LogoutHandler {

    private final OAuthService googleOAuthService;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        var authorizationHeader = request.getHeader("Authorization");

        if (!StringUtils.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            revokeGoogleAccessTokenIfMemberIsSingedUpByGoogle(authorizationHeader);
        }

        HttpSession session = request.getSession();
        if (Objects.nonNull(session)) {
            session.invalidate();
        }
    }

    private void revokeGoogleAccessTokenIfMemberIsSingedUpByGoogle(String accessToken) {
        try {
            Optional<Member> optionalMember = memberRepository.findById(tokenService.extractMemberIdByAccessToken(accessToken));
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                if (SignUpType.GOOGLE.equals(member.getSignUpType())) {
                    // 구글 회원의 경우 Access Token revoke
                    googleOAuthService.revokeAccessTokenForLogOut(accessToken);
                }
            }
        } catch (Exception e) {
            log.warn("MemberSignOutHandler.revokeGoogle.. : 구글 계정에 revoke 요청을 보내던 중 이슈가 발생했습니다.");
            log.warn(e.getMessage());
        }
    }

}
