package com.camping101.beta.member.service.mail;

import com.camping101.beta.member.entity.MailAuth;
import com.camping101.beta.util.CustomMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MailAuthSupporter {

    private final CustomMailSender customMailSender;

    @Value("${mailauth.expiration.time}")
    private long signUpMailAuthExpirationTime;

    public boolean isAuthCodeMatching(MailAuth mailAuth, String authCodeToCompare){

        if (Objects.nonNull(mailAuth.getMailAuthCode())) {
            return mailAuth.getMailAuthCode().equals(authCodeToCompare);
        }

        return false;
    }

    public boolean isAuthCodeExpired(MailAuth mailAuth) {

        return LocalDateTime.now().isAfter(
                mailAuth.getCreatedAt().plusSeconds(signUpMailAuthExpirationTime));
    }

    public boolean sendMailAuthCode(String receiver, String authCode){

        var subject = "[Camping101] 캠핑 101에서 인증 메일을 전송했습니다.";
        var htmlText = "<!DOCTYPE html>\n" +
                "<html lang=\"ko\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <form action = 'http://localhost:8080/api/signup/mail/auth'>\n" +
                "        <input type = hidden name = 'email' value = '" + receiver + "'>" +
                "        <input type = hidden name = 'authCode' value = '" + authCode + "'>" +
                "        <input type = 'submit' value = '인증하기'>\n" +
                "    </form>\n" +
                "</body>\n" +
                "</html>";

        return customMailSender.sendMail(subject, "", htmlText, receiver);
    }

}
