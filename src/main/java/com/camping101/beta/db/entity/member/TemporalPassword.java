package com.camping101.beta.db.entity.member;

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
@EntityListeners(value = {AuditingEntityListener.class})
public class TemporalPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_pswd_id")
    private Long id;
    private Long memberId;
    private String temporalPassword;

    @CreatedDate
    private LocalDateTime createdAt;

    public static TemporalPassword of(Long memberId, String temporalPassword) {
        return TemporalPassword.builder()
                .memberId(memberId)
                .temporalPassword(temporalPassword)
                .build();
    }

}
