package com.camping101.beta.web.domain.member.service;

import com.camping101.beta.web.domain.member.dto.MemberInfoResponse;
import com.camping101.beta.web.domain.member.dto.MemberUpdateRequest;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.TemporalPassword;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.repository.TemporalPasswordRepository;
import com.camping101.beta.util.CustomMailSender;
import com.camping101.beta.util.RandomCode;
import com.camping101.beta.util.S3FileUploader;
import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final TemporalPasswordRepository temporalPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomMailSender customMailSender;
    private final S3FileUploader s3FileUploader;

    @Override
    @Transactional
    public void sendTemporalPassword(String signInMemberEmail, Long pathMemberId){

        validateIfMemberIdMatchingWithPathMemberId(getMemberId(signInMemberEmail), pathMemberId);

        TemporalPassword tempPassword = createLimitedTimeTemporalPassword(pathMemberId);

        sendTemporalPasswordToMail(signInMemberEmail, tempPassword.getTemporalPassword());

    }

    private static void validateIfMemberIdMatchingWithPathMemberId(Long memberId, Long pathMemberId) {
        if (!Objects.equals(memberId, pathMemberId)) {
            throw new MemberException(ErrorCode.MEMBER_IS_NOT_MATCHING);
        }
    }

    private TemporalPassword createLimitedTimeTemporalPassword(Long memberId) {

        temporalPasswordRepository.findByMemberId(memberId).ifPresent(temporalPasswordRepository::delete);

        return temporalPasswordRepository.save(TemporalPassword.of(memberId, RandomCode.createRandomEightString()));
    }

    private void sendTemporalPasswordToMail(String email, String temporalPasswordCode) {

        String subject = "[Camping101] 캠핑 101에서 임시 비밀번호를 전송했습니다.";
        String htmlText = "<!DOCTYPE html>\n" +
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

        boolean mailSendResult = customMailSender.sendMail(subject, "", htmlText, email);

        if (!mailSendResult) {
            throw new MemberException(ErrorCode.TEMPORAL_PASSWORD_ISSUE_FAIL);
        }
    }

    @Override
    public Long getMemberId(String signInMemberEmail) {

        Member member = memberRepository.findByEmail(signInMemberEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        return member.getMemberId();
    }

    @Override
    public MemberInfoResponse getMemberInfo(String signInMemberEmail, Long pathMemberId) {

        Member member = memberRepository.findById(pathMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberIdMatchingWithPathMemberId(member.getMemberId(), pathMemberId);

        return MemberInfoResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberInfoResponse updateMember(String email, Long memberId, MemberUpdateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberIdMatchingWithPathMemberId(member.getMemberId(), memberId);
        validateIfMemberSignedUpByEmail(member);

        String newPassword = getNewPassword(member.getPassword(), request);
        String newProfileImagePath = getNewProfileImagePath(member.getImage(), request);
        String newNickname = getNewNickname(member.getNickname(), request);
        String newPhoneNumber = getNewPhoneNumber(member.getPhoneNumber(), request);

        member.changePassword(newPassword);
        member.changeImage(newProfileImagePath);
        member.changeNickname(newNickname);
        member.changePhoneNumber(newPhoneNumber);

        return MemberInfoResponse.fromEntity(member);
    }

    private String getNewPassword(String originalPassword, MemberUpdateRequest request) {
        return StringUtils.isNullOrEmpty(request.getPassword()) ?
                originalPassword : passwordEncoder.encode(request.getPassword());
    }

    private String getNewProfileImagePath(String originalProfileImage, MemberUpdateRequest request) {
        return StringUtils.isNullOrEmpty(originalProfileImage) ?
                originalProfileImage : s3FileUploader.uploadFileAndGetURL(request.getImage());
    }

    private String getNewNickname(String originalNickname, MemberUpdateRequest request) {
        return StringUtils.isNullOrEmpty(originalNickname) ?
                originalNickname : request.getNickname();
    }

    private String getNewPhoneNumber(String originalPhoneNumber, MemberUpdateRequest request) {
        return StringUtils.isNullOrEmpty(originalPhoneNumber) ?
                originalPhoneNumber : request.getPhoneNumber();
    }

    private void validateIfMemberSignedUpByEmail(Member member) {
        if (SignUpType.EMAIL.equals(member.getSignUpType())){
            throw new MemberException(ErrorCode.MEMBER_UPDATE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteMember(String signInMemberEmail, Long pathMemberId){

        Member member = memberRepository.findByEmail(signInMemberEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberIdMatchingWithPathMemberId(member.getMemberId(), pathMemberId);

        log.info("MemberServeImpl.deleteMember : 다음 회원이 탈퇴를 요청했습니다 >> " + pathMemberId);

        member.changeMemberStatus(MemberStatus.WITHDRAW);
        member.changeDeletedAt(LocalDateTime.now());

    }

}
