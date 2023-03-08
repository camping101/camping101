package com.camping101.beta.admin.member.dto;

import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.type.MemberType;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMemberListResponse {

    long total;
    int pageNumber = 0;
    int recordSize = 10;
    String memberType = MemberType.CUSTOMER.name();
    List<AdminMemberInfoResponse> content = new ArrayList<>();


    public static AdminMemberListResponse fromEntity(Page<Member> response) {
        return AdminMemberListResponse.builder()
                .total(response.getTotalElements())
                .pageNumber(response.getNumber())
                .recordSize(response.getNumberOfElements())
                .memberType(response.getContent().get(0).getMemberType().name())
                .content(response.getContent().stream()
                        .map(AdminMemberInfoResponse::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
