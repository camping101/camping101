package com.camping101.beta.web.domain.bookmark.dto;

import com.camping101.beta.db.entity.bookmark.BookMark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindBookMarkListRs {

    private Long bookMarkId;
    private Long memberId;
    private String nickName;

    private Long campLogId;
    private String title;
    private String description;
    private String image;

    public static FindBookMarkListRs createFindBookMarkListRs(BookMark bookMark) {

        return FindBookMarkListRs.builder()
            .bookMarkId(bookMark.getBookMarkId())
            .memberId(bookMark.getMember().getMemberId())
            .nickName(bookMark.getMember().getNickname())
            .campLogId(bookMark.getCampLog().getCampLogId())
            .title(bookMark.getCampLog().getTitle())
            .description(bookMark.getCampLog().getDescription())
            .image(bookMark.getCampLog().getImage())
            .build();

    }

}
