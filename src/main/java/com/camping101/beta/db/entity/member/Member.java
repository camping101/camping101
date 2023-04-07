package com.camping101.beta.db.entity.member;

import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.web.domain.member.dto.MemberSignUpRequest;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.web.domain.member.service.oAuth.GoogleAccountInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String phoneNumber;
    private String image;
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignUpType signUpType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

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

    public void changeImage(String image) {
        this.image = image;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void changeDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

}
