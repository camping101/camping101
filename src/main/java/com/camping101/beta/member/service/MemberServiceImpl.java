package com.camping101.beta.member.service;

import com.camping101.beta.member.dto.MemberInfoResponse;
import com.camping101.beta.member.dto.MemberPasswordResetResponse;
import com.camping101.beta.member.dto.MemberUpdateRequest;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.TemporalPassword;
import com.camping101.beta.member.entity.status.MemberStatus;
import com.camping101.beta.member.entity.type.SignUpType;
import com.camping101.beta.member.exception.ErrorCode;
import com.camping101.beta.member.exception.MemberException;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.member.repository.TemporalPasswordRepository;
import com.camping101.beta.util.CustomMailSender;
import com.camping101.beta.util.RandomCode;
import com.camping101.beta.util.S3FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final TemporalPasswordRepository temporalPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomMailSender customMailSender;
    private final S3FileUploader s3FileUploader;

    @Override
    public MemberInfoResponse getMemberInfo(String email, Long memberId) {

        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        if (member.getMemberId() != memberId) {
            throw new MemberException(ErrorCode.MEMBER_NOT_MATH);
        }

        var memberInfoResponse = MemberInfoResponse.fromEntity(member);

        return memberInfoResponse;
    }

    @Override
    @Transactional(rollbackOn = {MemberException.class})
    public MemberPasswordResetResponse sendTemporalPassword(String email){

        var member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberSignedUpByEmail(member);

        var TemporalPassword = createTemporalPasswordThatCanBeUsedWithinFiveMinutes(email);
        var mailSendResult = sendTemporalPasswordToMail(member.getEmail(), TemporalPassword.getTemporalPassword());

        if (mailSendResult) {
            return MemberPasswordResetResponse.builder()
                    .result("SUCCESS")
                    .message("Reset Password Sent To Mail " + member.getEmail())
                    .build();
        } else {
            throw new MemberException(ErrorCode.MEMBER_RESET_PASSWORD_FAIL);
        }
    }

    private void validateIfMemberSignedUpByEmail(Member member) {
        if (!SignUpType.EMAIL.equals(member.getSignUpType())) {
            throw new MemberException(ErrorCode.MEMBER_RESET_PASSWORD_FAIL);
        }
    }

    private TemporalPassword createTemporalPasswordThatCanBeUsedWithinFiveMinutes(String email) {

        var optionalTemporalPassword = temporalPasswordRepository.findByEmail(email);

        if (optionalTemporalPassword.isPresent()) {
            temporalPasswordRepository.delete(optionalTemporalPassword.get());
        }

        return temporalPasswordRepository.save(TemporalPassword.builder()
                .email(email)
                .temporalPassword(RandomCode.createRandomEightString())
                .emailSentAt(LocalDateTime.now())
                .activeYn(false)
                .build());
    }

    private boolean sendTemporalPasswordToMail(String email, String temporalPasswordCode) {

        var subject = "[Camping101] 캠핑 101에서 임시 비밀번호를 전송했습니다.";
        var htmlText = "<!DOCTYPE html>\n" +
                "<html lang=\"ko\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "        <div>"
                + "임시 비밀번호는 [ " + temporalPasswordCode + "] 입니다." +
                "        </div>" +
                "</body>\n" +
                "</html>";

        return customMailSender.sendMail(subject, "", htmlText, email);

    }

    @Override
    @Transactional
    public MemberInfoResponse updateMember(String email, Long memberId, MemberUpdateRequest request) {

        var member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        if (member.getMemberId() != memberId) {
            throw new MemberException(ErrorCode.MEMBER_NOT_MATH);
        }

        if (Objects.nonNull(request.getPassword())) {
            validateIfMemberSignedUpByEmail(member);
            member.setPassword(passwordEncoder.encode(member.getPhoneNumber()));
        }
        if (Objects.nonNull(request.getImage())) {
            var s3ImageUrl = s3FileUploader.uploadFileAndGetURL(request.getImage());
            member.setImage(s3ImageUrl);
        }
        if (Objects.nonNull(request.getNickname())) {
            member.setNickname(request.getNickname());
        }
        if (Objects.nonNull(request.getPhoneNumber())) {
            member.setPhoneNumber(request.getPhoneNumber());
        }

        return MemberInfoResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId){

        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        member.setMemberStatus(MemberStatus.WITHDRAW);
        member.setDeletedAt(LocalDateTime.now());

    }

}
