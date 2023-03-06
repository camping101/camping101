package com.camping101.beta.member.service;

import com.camping101.beta.member.dto.MemberSignUpRequest;
import com.camping101.beta.member.entity.MailAuth;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.exception.ErrorCode;
import com.camping101.beta.member.exception.MemberException;
import com.camping101.beta.member.repository.MailAuthRepository;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.member.service.mail.MailAuthSupporter;
import com.camping101.beta.util.RandomCode;
import com.camping101.beta.util.S3FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MemberSignUpServiceImpl implements MemberSignUpService{

    private final MemberRepository memberRepository;
    private final MailAuthRepository mailAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailAuthSupporter mailAuthSupporter;
    private final S3FileUploader s3FileUploader;
    private final Logger logger = Logger.getLogger(MemberSignUpServiceImpl.class.getName());

    @Override
    public void signUpByEmail(MemberSignUpRequest request) {

        validateIfMemberAlreadySignedUp(request.getEmail());

        try {
            var s3ImageUrl = s3FileUploader.uploadFileAndGetURL(request.getImage());
            var newMember = createNewMember(request, s3ImageUrl);
            var mailAuth = createSignUpMailAuthCode(newMember.getEmail());
            mailAuthSupporter.sendMailAuthCode(request.getEmail(), mailAuth.getMailAuthCode());
        } catch (Exception e) {
            logger.info("회원가입 중 이슈 발생");
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.warning(x.toString()));
        }
    }

    private void validateIfMemberAlreadySignedUp(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isPresent()) {
            new MemberException(ErrorCode.MEMBER_SIGN_UP_ERROR);
        }
    }

    private Member createNewMember(MemberSignUpRequest request, String s3ImageUrl){
        var encPassword = passwordEncoder.encode(request.getPassword());
        var newMember = Member.from(request, s3ImageUrl, encPassword);
        return memberRepository.save(newMember);
    }

    private MailAuth createSignUpMailAuthCode(String email) throws Exception{
        String mailAuthCode = RandomCode.createRandomUUID();
        return mailAuthRepository.save(MailAuth.from(email, mailAuthCode));
    }

    public void signUpByMember(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void activateMemberByAuthCode(String email, String requestedCode) {

        var member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_MAIL_AUTH_FAIL));

        var optionalMailAuth = mailAuthRepository.findByEmail(email);

        if (optionalMailAuth.isEmpty()) {
            try {
                var mailAuth = createSignUpMailAuthCode(member.getEmail());
                mailAuthSupporter.sendMailAuthCode(member.getEmail(), mailAuth.getMailAuthCode());
                throw new MemberException(ErrorCode.MEMBER_MAIL_AUTH_RENEW);
            } catch (Exception e) {
                throw new MemberException(ErrorCode.MEMBER_MAIL_AUTH_FAIL);
            }
        }

        var mailAuth = optionalMailAuth.get();
        validateMailAuthCodeAndResendMailIfRequired(requestedCode, mailAuth);

        member.activateMember();
        mailAuthRepository.delete(mailAuth);
    }

    private void validateMailAuthCodeAndResendMailIfRequired(String requestedCode, MailAuth mailAuth) {
        if (!mailAuthSupporter.isAuthCodeMatching(mailAuth, requestedCode)) {
            throw new MemberException(ErrorCode.MEMBER_MAIL_AUTH_FAIL);
        }

        if (mailAuthSupporter.isAuthCodeExpired(mailAuth)) {
            renewSignUpMailAuthCode(mailAuth);
            mailAuthSupporter.sendMailAuthCode(mailAuth.getEmail(), mailAuth.getMailAuthCode());
            throw new MemberException(ErrorCode.MEMBER_MAIL_AUTH_RENEW);
        }
    }

    private void renewSignUpMailAuthCode(MailAuth mailAuth){

        mailAuth.setMailAuthCode(RandomCode.createRandomUUID());
        mailAuth.setCreatedAt(LocalDateTime.now());
        mailAuthRepository.save(mailAuth);

    }

}
