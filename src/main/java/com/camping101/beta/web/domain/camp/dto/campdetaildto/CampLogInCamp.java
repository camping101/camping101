package com.camping101.beta.web.domain.camp.dto.campdetaildto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CampLogInCamp {

    private Long memberId;
    private Long siteId;
    private Long campLogId;
    private String campLogName;
    private String description; // 최대 200자
    private LocalDateTime visitedAt; // 이떄 갔어요.
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public CampLogInCamp(Long memberId, Long siteId, Long campLogId, String campLogName,
        String description, LocalDateTime visitedAt, String image, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.memberId = memberId;
        this.siteId = siteId;
        this.campLogId = campLogId;
        this.campLogName = campLogName;
        this.description = description;
        this.visitedAt = visitedAt;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
