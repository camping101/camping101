package com.camping101.beta.bookMark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookMarkCreateRequest {

    private Long memberId;
    private Long campLogId;


}
