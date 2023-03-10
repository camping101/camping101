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
import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final TemporalPasswordRepository temporalPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomMailSender customMailSender;
    private final S3FileUploader s3FileUploader;
    private final Logger logger = Logger.getLogger(MemberServiceImpl.class.getName());

    @Override
    public MemberInfoResponse getMemberInfo(String email, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        if (member.getMemberId() != memberId) {
            throw new MemberException(ErrorCode.MEMBER_NOT_MATH);
        }

        MemberInfoResponse memberInfoResponse = MemberInfoResponse.fromEntity(member);

        return memberInfoResponse;
    }

    @Override
    @Transactional(rollbackOn = {MemberException.class})
    public MemberPasswordResetResponse sendTemporalPassword(String email){

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberSignedUpByEmail(member);

        TemporalPassword TemporalPassword = createTemporalPasswordThatCanBeUsedWithinFiveMinutes(email);
        boolean mailSendResult = sendTemporalPasswordToMail(member.getEmail(), TemporalPassword.getTemporalPassword());

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

            logger.info("MemberServiceImpl.updateMember.validateIfMemberSignedUpByEmail : " +
                    "????????? ????????? ???????????? ????????? ???????????????. (OAuth??? ?????? ?????? ????????? ?????? ?????? ??????)");

            throw new MemberException(ErrorCode.MEMBER_RESET_PASSWORD_FAIL);
        }
    }

    private TemporalPassword createTemporalPasswordThatCanBeUsedWithinFiveMinutes(String email) {

        Optional<TemporalPassword> optionalTemporalPassword = temporalPasswordRepository.findByEmail(email);

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

        String subject = "[Camping101] ?????? 101?????? ?????? ??????????????? ??????????????????.";
        String htmlText = "<!DOCTYPE html>\n" +
                "<html lang=\"ko\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "        <div>"
                + "?????? ??????????????? [ " + temporalPasswordCode + "] ?????????." +
                "        </div>" +
                "</body>\n" +
                "</html>";

        return customMailSender.sendMail(subject, "", htmlText, email);


    }

    @Override
    @Transactional
    public MemberInfoResponse updateMember(String email, Long memberId, MemberUpdateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        logger.info("MemberServeImpl.updateMember?????? ?????? ????????? ????????? ??????????????????. >>" + memberId);

        if (member.getMemberId() != memberId) {

            logger.info("MemberServiceImpl.updateMember : ????????? ???????????? ????????? ???????????????.");

            throw new MemberException(ErrorCode.MEMBER_NOT_MATH);
        }

        if (Objects.nonNull(request.getPassword())) {
            validateIfMemberSignedUpByEmail(member);
            member.changePassword(passwordEncoder.encode(member.getPassword()));
            logger.info("MemberServeImpl.updateMember : ??????????????? ??????????????????.");
        }
        if (Objects.nonNull(request.getImage())) {
            String s3ImageUrl = s3FileUploader.uploadFileAndGetURL(request.getImage());

            if (StringUtils.isNullOrEmpty(s3ImageUrl)) {
                logger.info("MemberServeImpl.updateMember : ????????? ?????? ????????? ???????????? ?????? ???????????? ???????????????.");
            } else {
                member.changeImage(s3ImageUrl);
            }
        }
        if (Objects.nonNull(request.getNickname())) {
            logger.info("MemberServeImpl.updateMember : ???????????? ??????????????????.");
            member.changeNickname(request.getNickname());
        }
        if (Objects.nonNull(request.getPhoneNumber())) {
            logger.info("MemberServeImpl.updateMember : ??????????????? ??????????????????.");
            member.changePhoneNumber(request.getPhoneNumber());
        }

        return MemberInfoResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId, String requesterEmail){

        Member member = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        if (member.getMemberId() != memberId) {
            logger.info("MemberServiceImpl.deleteMember : ????????? ????????? ?????? ????????? ??? ????????????.");
            throw new MemberException(ErrorCode.MEMBER_NOT_MATH);
        }

        logger.info("MemberServeImpl.deleteMember : ?????? ????????? ????????? ?????????????????? >> " + memberId);

        member.changeMemberStatus(MemberStatus.WITHDRAW);
        member.changeDeletedAt(LocalDateTime.now());

    }

}
