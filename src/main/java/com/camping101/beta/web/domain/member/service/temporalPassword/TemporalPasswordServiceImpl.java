package com.camping101.beta.web.domain.member.service.temporalPassword;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.TemporalPassword;
import com.camping101.beta.util.CustomMailSender;
import com.camping101.beta.util.RandomCode;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.repository.TemporalPasswordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemporalPasswordServiceImpl implements TemporalPasswordService{

    private final TemporalPasswordRepository temporalPasswordRepository;
    private final MemberRepository memberRepository;
    private final CustomMailSender customMailSender;

    @Value("${temporal.password.expiration.seconds}")
    private long temporalPasswordExpirationSeconds;

    @Override
    public void sendTemporalPassword(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        TemporalPassword tempPassword = createLimitedTimeTemporalPassword(member);

        sendTemporalPasswordToMail(member.getEmail(), tempPassword.getTemporalPassword());

    }

    private TemporalPassword createLimitedTimeTemporalPassword(Member member) {

        temporalPasswordRepository.findByMember(member).ifPresent(temporalPasswordRepository::delete);

        String randomTemporalPasswordCode = RandomCode.createRandomEightString();
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(temporalPasswordExpirationSeconds);

        return temporalPasswordRepository.save(TemporalPassword.from(member, randomTemporalPasswordCode, expiredAt));
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
    public boolean isTemporalPasswordMatching(Long memberId, String temporalPasswordToCompare) {

        Optional<TemporalPassword> optionalTemporalPassword = temporalPasswordRepository.findByMemberId(memberId);

        if (optionalTemporalPassword.isEmpty()) return false;

        TemporalPassword temporalPassword = optionalTemporalPassword.get();

        if (temporalPassword.getExpiredAt().isBefore(LocalDateTime.now())) return false;

        return StringUtils.equals(temporalPassword.getTemporalPassword(), temporalPasswordToCompare);
    }

}
