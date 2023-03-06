package com.camping101.beta.member.dto;

import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.entity.type.SignUpType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoResponse {

    private Long memberId;
    private String image;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String memberType;
    private String signUpType;

    public static MemberInfoResponse fromEntity(Member member) {
        return MemberInfoResponse.builder()
                .memberId(member.getMemberId())
                .image(member.getImage())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .memberType(member.getMemberType().name())
                .signUpType(member.getSignUpType().name())
                .build();
    }
}
