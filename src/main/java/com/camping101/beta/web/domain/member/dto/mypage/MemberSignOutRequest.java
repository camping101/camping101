package com.camping101.beta.web.domain.member.dto.mypage;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberSignOutRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String accessToken;

}
