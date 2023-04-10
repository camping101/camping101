package com.camping101.beta.db.entity.member;

import com.camping101.beta.db.entity.BaseEntity;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class MailAuth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_auth_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private String mailAuthCode;

    public static MailAuth from(Member member, String mailAuthCode) {
        return MailAuth.builder()
                .member(member)
                .mailAuthCode(mailAuthCode)
                .build();
    }

}
