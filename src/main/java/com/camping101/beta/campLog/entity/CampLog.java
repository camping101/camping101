package com.camping101.beta.campLog.entity;

import com.camping101.beta.bookMark.entity.BookMark;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.site.entity.Site;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class CampLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_log_id")
    private Long campLogId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_mark_id")
    private BookMark bookMark;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

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
    @Column(updatable = false, insertable = true)
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

    // TODO 사이트 연관관계 추가 필요
//    public void changeSite(Site site) {
//        this.site = site;
//        if (!site.getCampLogs().contains(this)) {
//            site.getCampLogs().add(this);
//        }
//    }

}
