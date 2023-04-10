package com.camping101.beta.db.entity.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash(value = "refreshToken")
@AllArgsConstructor
@Builder
@Getter
public class RefreshToken {

    @Id
    private String refreshToken;
    private String googleRefreshToken;
    private Long memberId;
    private LocalDateTime expiredAt;

}
