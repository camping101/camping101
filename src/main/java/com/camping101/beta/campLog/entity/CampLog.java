package com.camping101.beta.campLog.entity;

import com.camping101.beta.bookMark.entity.BookMark;
import com.camping101.beta.campLog.dto.CampLogCreateRequest;
import com.camping101.beta.campLog.dto.CampLogUpdateRequest;
import com.camping101.beta.comment.entity.Comment;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.site.entity.Site;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    private String recTags;
    private String campLogName; // 내가 일단 임의로 추가함

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

    private long likes;
    private long view;

    @OneToMany(mappedBy = "campLog")
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "campLog")
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void changeMember(Member member) {
        this.member = member;
        if (!member.getCampLogs().contains(this)) {
            member.getCampLogs().add(this);
        }
    }
    public static CampLog from(CampLogCreateRequest request) {
        return CampLog.builder()
            .visitedAt(request.getVisitedAt())
            .visitedWith(request.getVisitedWith())
            .title(request.getTitle())
            .description(request.getDescription())
            .build();
    }

    public void setImagePaths(List<String> imagePaths) {
        this.image = imagePaths.get(0);
        this.image1 = imagePaths.get(1);
        this.image2 = imagePaths.get(2);
        this.image3 = imagePaths.get(3);
        this.image4 = imagePaths.get(4);
        this.image5 = imagePaths.get(5);
    }

    public void setRecTags(String recTags){
        this.recTags = recTags;
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getCampLogs().contains(this)) {
            member.getCampLogs().add(this);
        }
    }

    public void addBookMark(BookMark bookMark) {
        this.bookMarks.add(bookMark);
        if (bookMark.getCampLog() != this) {
            bookMark.setCampLog(this);
        }
    }

    public void setSite(Site site) {
        this.site = site;
//        if (!site.getCamLogs().contains(this)) {
//            site.getCampLogs().add(this);
//        }
    }
    public void addComment(Comment comment) {
        this.comments.add(comment);
        if (comment.getCampLog() != this) {
            comment.setCampLog(this);
        }
    }

    public void increaseViewCount() {
        this.view = this.view + 1;
    }

    public void updateCampLog(CampLogUpdateRequest request) {
        this.visitedAt = request.getVisitedAt();
        this.visitedWith = request.getVisitedWith();
        this.title = request.getTitle();
        this.description = request.getDescription();
    }

    public void increaseLikesCount() {
        this.likes = this.likes + 1;
    }

    public void decreaseLikesCount() {
        this.likes = this.likes + 1;
    }

}
