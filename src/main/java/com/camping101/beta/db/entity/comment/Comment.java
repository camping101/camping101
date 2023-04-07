package com.camping101.beta.db.entity.comment;

import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.web.domain.comment.dto.CommentCreateRequest;
import com.camping101.beta.db.entity.member.Member;
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

    private long parentId = -1L;
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

    public void changeMember(Member member) {
        this.member = member;
    }

    public void changeCampLog(CampLog campLog) {
        this.campLog = campLog;
        if (!campLog.getComments().contains(this)) {
            campLog.addComment(this);
        }
    }

    public void changeContent(String content) {
        this.content = content;
    }

}