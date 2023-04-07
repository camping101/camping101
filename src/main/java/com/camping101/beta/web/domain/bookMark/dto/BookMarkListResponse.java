package com.camping101.beta.web.domain.bookMark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookMarkListResponse {

    private Long bookMarkId;
    private Long memberId;
    private String nickName;

    private Long campLogId;
    private String title;
    private String description;
    private String image;

}
