package com.camping101.beta.member.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners({AuditingEntityListener.class})
public class MailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_auth_id")
    private Long id;

    private String email;
    private String mailAuthCode;
    @CreatedDate
    private LocalDateTime createdAt;

    public static MailAuth from(String email, String mailAuthCode) {
        return MailAuth.builder()
                .email(email)
                .mailAuthCode(mailAuthCode)
                .build();
    }

}
