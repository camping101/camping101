package com.camping101.beta.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class CampLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campLogId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "BOOK_MARK_ID")
    private BookMark bookMark;

    // TODO 사이트 참조 추가 필요

    @OneToMany(mappedBy = "campLog")
    private List<RecTag> recTags = new ArrayList<RecTag>();

    private LocalDateTime visitedAt;
    private String visitedWith;
    private String title;
    private String description; // 최대 200자
    private String image;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

    private long like;
    private long view;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addRecTag(RecTag recTag) {
        this.recTags.add(recTag);
        if (recTag.getCampLog() != this) {
            recTag.changeCampLog(this);
        }
    }

    public void changeMember(Member member) {
        this.member = member;
        if (!member.getCampLogs().contains(this)) {
            member.getCampLogs().add(this);
        }
    }

    public void changeBookMark(BookMark bookMark) {
        this.bookMark = bookMark;
        if (!bookMark.getCampLogs().contains(this)) {
            bookMark.getCampLogs().add(this);
        }
    }

}
