package com.camping101.beta.bookMark.entity;

import com.camping101.beta.bookMark.dto.BookMarkCreateResponse;
import com.camping101.beta.bookMark.dto.BookMarkListResponse;
import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.member.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @JoinColumn(name = "camp_log_id")
    private CampLog campLog;

    public void changeCampLog(CampLog campLog) {
        this.campLog = campLog;
        if (!campLog.getBookMarks().contains(this)) {
            campLog.addBookMark(this);
        }
    }

    public void changeMember(Member member) {
        this.member = member;
    }

    public static BookMarkCreateResponse toBookMarkCreateResponse(BookMark bookMark) {

        return BookMarkCreateResponse.builder()
            .bookMarkId(bookMark.getBookMarkId())
            .memberId(bookMark.getMember().getMemberId())
            .campLogId(bookMark.getCampLog().getCampLogId())
            //.campLogName(bookMark.getCampLog().getCampLogName())
            .build();

    }

    public static BookMarkListResponse toBookMarkListResponse(BookMark bookMark) {

        return BookMarkListResponse.builder()
            .bookMarkId(bookMark.getBookMarkId())
            .memberId(bookMark.getMember().getMemberId())
            //.nickName(bookMark.getMember().getNickName())
            .campLogId(bookMark.getCampLog().getCampLogId())
            //.campLogName(bookMark.getCampLog().getCampLogName())
            .title(bookMark.getCampLog().getTitle())
            .description(bookMark.getCampLog().getDescription())
            .image(bookMark.getCampLog().getImage())
            .build();

    }


}
