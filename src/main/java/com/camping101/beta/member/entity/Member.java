package com.camping101.beta.member.entity;

import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.comment.entity.Comment;
import com.camping101.beta.member.dto.MemberSignUpRequest;
import com.camping101.beta.member.entity.status.MemberStatus;
import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.entity.type.SignInType;
import com.camping101.beta.member.entity.type.SignUpType;
import com.camping101.beta.member.service.oAuth.GoogleAccountInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @OneToMany(mappedBy = "member")
    private List<CampLog> campLogs = new ArrayList<CampLog>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<Comment>();

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = true)
    private LocalDateTime deletedAt;
    private String nickname;

    @Enumerated
    private SignUpType signUpType;

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

    public void activateMember() {
        this.memberStatus = MemberStatus.IN_USE;
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
