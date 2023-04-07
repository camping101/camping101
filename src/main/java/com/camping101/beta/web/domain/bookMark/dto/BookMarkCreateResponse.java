package com.camping101.beta.web.domain.bookMark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookMarkCreateResponse {

    private Long bookMarkId;
    private Long memberId;
    private Long campLogId;
    private String title;

}
