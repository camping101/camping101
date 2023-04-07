package com.camping101.beta.web.domain.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14) // 2 week
@AllArgsConstructor
@Builder
@Getter
public class RefreshToken {

    @Id
    private String refreshToken;
    private String googleRefreshToken;
    private Long memberId;
    private String email;
    private String memberType;

}
