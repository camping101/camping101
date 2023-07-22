package com.camping101.beta.db.entity.comment;

import com.camping101.beta.db.entity.member.Member;
import java.time.LocalDateTime;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class ReComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reComment_id")
    private Long reCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    Comment parentComment;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static ReComment from(Comment parentComment, Member member, String content) {
        return ReComment.builder()
            .member(member)
            .parentComment(parentComment)
            .content(content)
            .build();
    }

    public void changeContent(String content) {
        this.content = content;
    }

}