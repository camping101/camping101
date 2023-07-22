package com.camping101.beta.db.entity.comment;

import com.camping101.beta.db.entity.camplog.CampLog;
import com.camping101.beta.db.entity.member.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    List<ReComment> reComments = new ArrayList<>();

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Comment from(CampLog campLog, Member commentWriter, String content) {
        return Comment.builder()
                .campLog(campLog)
                .member(commentWriter)
                .content(content)
                .reComments(new ArrayList<>())
                .build();
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

    public void changeReComments(List<ReComment> reComments) {
        this.reComments = reComments;
    }

}