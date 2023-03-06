package com.camping101.beta.admin.dto;

import com.camping101.beta.admin.status.CampAuthStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampAuthListResponse {

    private Long campAuthId;
    private Long campId;
    private String campName;
    private String campAuthStatus;




}
