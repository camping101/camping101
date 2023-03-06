package com.camping101.beta.comment.entity;

import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.comment.dto.CommentCreateRequest;
import com.camping101.beta.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne
    @JoinColumn(name = "camp_log_id")
    CampLog campLog;

    private Long parentId;
    private boolean reCommentYn;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Comment from(CommentCreateRequest request) {
        return Comment.builder()
                .parentId(request.getParentId())
                .reCommentYn(request.isReCommentYn())
                .content(request.getContent())
                .build();
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getComments().contains(this)) {
            member.addComment(this);
        }
    }

    public void setCampLog(CampLog campLog) {
        this.campLog = campLog;
        if (!campLog.getComments().contains(this)) {
            campLog.addComment(this);
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

}
