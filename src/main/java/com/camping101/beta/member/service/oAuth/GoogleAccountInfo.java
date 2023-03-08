package com.camping101.beta.member.service.oAuth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;

}
