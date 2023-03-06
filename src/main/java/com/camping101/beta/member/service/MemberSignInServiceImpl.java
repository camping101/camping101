package com.camping101.beta.member.service;

import com.camping101.beta.member.dto.MemberSignInRequest;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.TemporalPassword;
import com.camping101.beta.member.exception.ErrorCode;
import com.camping101.beta.member.exception.MemberException;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.member.repository.TemporalPasswordRepository;
import com.camping101.beta.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MemberSignInServiceImpl implements MemberSignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemporalPasswordRepository temporalPasswordRepository;
    private final Logger logger = Logger.getLogger(MemberSignInServiceImpl.class.getName());

    @Value("${password.reset.seconds}")
    private long PASSWORD_RESET_TIME;

    @Override
    public void authenticateRequest(MemberSignInRequest request) throws AuthenticationException {

        var member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("MEMBER NOT FOUND"));

        var optionalTemporalPassword = temporalPasswordRepository.findByEmail(request.getEmail());

        if (optionalTemporalPassword.isPresent()) {
            var temporalPassword = optionalTemporalPassword.get();
            if (temporalPassword.getTemporalPassword().equals(request.getPassword())) {
                validateIfTemporalPasswordCanBeActivated(temporalPassword);
                logger.info(member.getEmail() + " 의 임시 비밀번호는 : " + temporalPassword.getTemporalPassword());
            }
        } else {
            if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
                logger.info("MemberSignInServiceImpl : 비밀번호 불일치 (EMAIL : " + member.getEmail() + ")");
                throw new BadCredentialsException("BAD CREDENTIAL");
            }
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR));

        return new MemberDetails(member);
    }

    private void validateIfTemporalPasswordCanBeActivated(TemporalPassword temporalPassword){
        if (temporalPassword.isActiveYn()) {
            throw new MemberException(ErrorCode.MEMBER_RESET_PASSWORD_FAIL);
        }
        if (LocalDateTime.now().isAfter(temporalPassword.getEmailSentAt().plusSeconds(PASSWORD_RESET_TIME))) {
            throw new MemberException(ErrorCode.MEMBER_RESET_PASSWORD_FAIL);
        }
    }

}
