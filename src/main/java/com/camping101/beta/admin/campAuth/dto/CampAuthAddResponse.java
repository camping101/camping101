package com.camping101.beta.admin.campAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CampAuthAddResponse {

    private Long campAuthId;
    private Long campId;
    private String campName;
    private String campAuthStatus;

}
