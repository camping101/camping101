package com.camping101.beta.member.dto;

import com.camping101.beta.member.entity.type.MemberType;
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
