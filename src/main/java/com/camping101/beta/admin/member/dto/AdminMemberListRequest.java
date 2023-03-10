package com.camping101.beta.admin.member.dto;

import com.camping101.beta.member.entity.type.MemberType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMemberListRequest {

    int pageNumber = 0;
    int recordSize = 10;
    MemberType memberType = MemberType.CUSTOMER;

}
