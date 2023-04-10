package com.camping101.beta.web.domain.member.dto.mypage;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateRequest {

    private MultipartFile profileImage;
    private String nickname;
    private String phoneNumber;
    private String password;

}
