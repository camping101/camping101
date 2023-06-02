package com.camping101.beta.web.domain.admin.member.service;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.web.domain.member.dto.signin.SignInByEmailRequest;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.service.temporalPassword.TemporalPasswordService;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.camping101.beta.web.domain.member.exception.ErrorCode.MEMBER_SIGN_IN_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMemberSignInServiceImpl implements AdminMemberSignInService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemporalPasswordService temporalPasswordService;
    private final TokenService tokenService;

    @Override
    public TokenInfo signInByEmail(SignInByEmailRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberNotAdmin(member);
        validateIfMemberSignedUpByEmail(member);
        validateIfPasswordMatching(request, member);

        String accessToken = tokenService.createAccessToken(member);
        String refreshToken = tokenService.createRefreshToken(member);

        return new TokenInfo(accessToken, refreshToken);
    }

    private static void validateIfMemberNotAdmin(Member member) {
        if (!MemberType.ADMIN.equals(member.getMemberType())) {
            throw new MemberException(MEMBER_SIGN_IN_ERROR);
        }
    }

    private void validateIfMemberSignedUpByEmail(Member member) {

        if (!SignUpType.EMAIL.equals(member.getSignUpType())) {

            log.info("MemberSignInServiceImpl.validateIfMemberSignedUpByEmail: 이메일 회원이 아닙니다.");

            throw new MemberException(MEMBER_SIGN_IN_ERROR);
        }

    }

    private void validateIfPasswordMatching(SignInByEmailRequest request, Member member) {
        if (!isPasswordMatching(member.getMemberId(), request.getPassword(), member.getPassword())) {
            log.info("MemberSignInServiceImpl,signInByEmail : 비밀번호 불일치");
            throw new BadCredentialsException("비밀번호 불일치");
        }
    }

    @Override
    public boolean isPasswordMatching(Long memberId, String rawPassword, String encodedPassword) {

        if (temporalPasswordService.isTemporalPasswordMatching(rawPassword, memberId)) {
            log.info("MemberSignInServiceImpl.isPasswordMatching : 임시 비밀번호가 일치합니다.");
            return true;
        }

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
