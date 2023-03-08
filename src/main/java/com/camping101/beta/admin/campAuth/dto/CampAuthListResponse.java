package com.camping101.beta.admin.campAuth.dto;

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
