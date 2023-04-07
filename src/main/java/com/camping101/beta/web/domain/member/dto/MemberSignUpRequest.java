package com.camping101.beta.web.domain.member.dto;

import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberSignUpRequest {

    private MultipartFile image;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String nickname;
    private String phoneNumber;
    private SignUpType signUpType;
    private MemberType memberType;

}
