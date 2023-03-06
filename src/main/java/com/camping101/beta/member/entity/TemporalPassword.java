package com.camping101.beta.member.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemporalPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporal_password_id")
    private Long id;

    private String email;
    private String temporalPassword;
    private boolean activeYn;
    private LocalDateTime emailSentAt;

}
