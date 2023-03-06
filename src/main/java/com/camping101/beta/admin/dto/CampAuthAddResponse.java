package com.camping101.beta.admin.dto;

import com.camping101.beta.admin.status.CampAuthStatus;
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
