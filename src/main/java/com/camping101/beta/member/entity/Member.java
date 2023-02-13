package com.camping101.beta.member.entity;

import com.camping101.beta.bookMark.entity.BookMark;
import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.comment.entity.Comment;
import com.camping101.beta.member.entity.status.MemberStatus;
import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.entity.type.SignInType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    private String email;
    private String password;
    private String phoneNumber;
    private SignInType signInType;
    private MemberType memberType;
    private MemberStatus memberStatus;

    private String image;

    @OneToMany(mappedBy="member")
    private List<CampLog> campLogs = new ArrayList<CampLog>();

    @OneToMany(mappedBy="member")
    private List<Comment> comments = new ArrayList<Comment>();

    @OneToOne(mappedBy="member")
    private BookMark bookMark;

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = true)
    private LocalDateTime deletedAt;

    public void addCampLog(CampLog campLog) {
        this.campLogs.add(campLog);
        if (campLog.getMember() != this) {
            campLog.changeMember(this);
        }
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        if (comment.getMember() != this) {
            comment.changeComment(this);
        }
    }

    public void changeBookMark(BookMark bookMark) {
        this.bookMark = bookMark;
        if (bookMark.getMember() != this) {
            bookMark.changeMember(this);
        }
    }
}
