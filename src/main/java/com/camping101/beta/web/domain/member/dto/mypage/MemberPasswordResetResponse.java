package com.camping101.beta.web.domain.member.dto.mypage;

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
