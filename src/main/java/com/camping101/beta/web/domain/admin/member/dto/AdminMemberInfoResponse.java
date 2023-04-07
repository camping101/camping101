package com.camping101.beta.web.domain.admin.member.dto;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMemberInfoResponse {

    private Long memberId;
    private String image;
    private String email;
    private String nickname;
    private String phoneNumber;
    private SignUpType signUpType;
    private MemberType memberType;
    private MemberStatus memberStatus;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public static AdminMemberInfoResponse fromEntity(Member member) {
        return AdminMemberInfoResponse.builder()
                .memberId(member.getMemberId())
                .image(member.getImage())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .memberType(member.getMemberType())
                .signUpType(member.getSignUpType())
                .memberStatus(member.getMemberStatus())
                .createdAt(member.getCreatedAt())
                .deletedAt(member.getDeletedAt())
                .build();
    }

}
