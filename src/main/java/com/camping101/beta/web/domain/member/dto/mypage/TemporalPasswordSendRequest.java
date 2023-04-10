package com.camping101.beta.web.domain.member.dto.mypage;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TemporalPasswordSendRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

}
