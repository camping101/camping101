package com.camping101.beta.db.entity.member;

import com.camping101.beta.db.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class TemporalPassword extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_pswd_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private String temporalPassword;
    private LocalDateTime expiredAt;

    public static TemporalPassword from(Member member, String temporalPassword, LocalDateTime expiredAt) {
        return TemporalPassword.builder()
                .member(member)
                .temporalPassword(temporalPassword)
                .expiredAt(expiredAt)
                .build();
    }

}
