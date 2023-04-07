package com.camping101.beta.web.domain.member.dto;

import com.camping101.beta.db.entity.member.type.MemberType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSignInRequest {

    private String email;
    private String password;
    private MemberType memberType;

}
