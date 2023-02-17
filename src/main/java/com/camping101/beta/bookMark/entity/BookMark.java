package com.camping101.beta.bookMark.entity;

import com.camping101.beta.bookMark.dto.BookMarkCreateRequest;
import com.camping101.beta.bookMark.dto.BookMarkCreateResponse;
import com.camping101.beta.bookMark.dto.BookMarkListResponse;
import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_mark_id")
    private Long bookMarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private CampLog campLog;

    public void addCampLog(CampLog campLog) {
        this.campLog = campLog;
//        if (campLog.getBookMark() != this) {
//            campLog.changeBookMark(this);
//        }
    }

    public void changeMember(Member member) {
        this.member = member;
//        if (member.getBookMark() != this) {
//            member.changeBookMark(this);
//        }
    }

    public static BookMarkCreateResponse toBookMarkCreateResponse(BookMark bookMark) {

        return BookMarkCreateResponse.builder()
            .bookMarkId(bookMark.bookMarkId)
            .memberId(bookMark.getMember().getMemberId())
            .campLogId(bookMark.getCampLog().getCampLogId())
            .build();

    }

    public static BookMarkListResponse toBookMarkListResponse(BookMark bookMark) {

        return BookMarkListResponse.builder()
            .bookMarkId(bookMark.bookMarkId)
            .memberId(bookMark.getMember().getMemberId())
            .campLogId(bookMark.getCampLog().getCampLogId())
            .build();

    }


}
