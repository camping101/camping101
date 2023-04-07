package com.camping101.beta.web.domain.campLog.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampLogLikeResponse {

    private Long campLogId;
    private long totalLikes;
    private boolean myLikeCheck;

}
