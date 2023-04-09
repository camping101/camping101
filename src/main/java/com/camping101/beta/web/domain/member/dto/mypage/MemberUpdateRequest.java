package com.camping101.beta.web.domain.member.dto.mypage;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultipartFile image;
    private String nickname;
    private String phoneNumber;
    private String password;

}
