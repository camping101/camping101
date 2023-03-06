package com.camping101.beta.member.entity;

import com.camping101.beta.bookMark.entity.BookMark;
import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.comment.entity.Comment;
import com.camping101.beta.member.dto.MemberSignUpRequest;
import com.camping101.beta.member.entity.status.MemberStatus;
import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.entity.type.SignUpType;
import com.camping101.beta.member.service.oAuth.GoogleAccountInfo;
import lombok.*;
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
    private String nickname;
    @Enumerated(EnumType.STRING)
    private SignUpType signUpType;
    private String googleId;
    @Enumerated(EnumType.STRING)
    private MemberType memberType;
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;
    private String image;

    // TODO 주인 캠핑장 정보 추가

    @OneToMany(mappedBy="member")
    private List<CampLog> campLogs = new ArrayList<CampLog>();

    @OneToMany(mappedBy="member")
    private List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy="member")
    private List<BookMark> bookMarks = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = true)
    private LocalDateTime deletedAt;

    // TODO 주인 회원 캠핑장 연관관계 추가

    public static Member from(MemberSignUpRequest memberSignUpRequest,
                              String s3Url, String encPassword) {
        return Member.builder()
                .image(s3Url)
                .email(memberSignUpRequest.getEmail())
                .password(encPassword)
                .phoneNumber(memberSignUpRequest.getPhoneNumber())
                .nickname(memberSignUpRequest.getNickname())
                .signUpType(memberSignUpRequest.getSignUpType())
                .memberType(memberSignUpRequest.getMemberType())
                .memberType(MemberType.CUSTOMER)
                .memberStatus(MemberStatus.NOT_ACTIVATED)
                .build();
    }

    public static Member from(GoogleAccountInfo member) {
        return Member.builder()
                .image(member.getPicture())
                .email(member.getEmail())
                .nickname(member.getName())
                .signUpType(SignUpType.GOOGLE)
                .memberType(MemberType.CUSTOMER)
                .memberStatus(MemberStatus.IN_USE)
                .build();
    }

    public void activateMember()  {
        this.memberStatus = MemberStatus.IN_USE;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
        if (comment.getMember() != this) {
            comment.setMember(this);
        }
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
