package com.camping101.beta.web.domain.member.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateRequest {

    private MultipartFile image;
    private String nickname;
    private String phoneNumber;
    private String password;

}
