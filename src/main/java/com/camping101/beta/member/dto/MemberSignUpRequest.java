package com.camping101.beta.member.dto;

import com.camping101.beta.member.entity.type.MemberType;
import com.camping101.beta.member.entity.type.SignUpType;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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
