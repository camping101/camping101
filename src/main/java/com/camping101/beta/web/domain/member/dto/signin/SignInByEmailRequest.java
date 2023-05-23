package com.camping101.beta.web.domain.member.dto.signin;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignInByEmailRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String email;
    @NotBlank
    private String password;

}
