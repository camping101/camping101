package com.camping101.beta.member.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPasswordResetResponse {

    private String result;
    private String message;

}
