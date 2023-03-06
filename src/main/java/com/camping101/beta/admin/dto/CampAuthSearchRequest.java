package com.camping101.beta.admin.dto;

import com.camping101.beta.camp.entity.Location;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CampAuthSearchRequest {

    // 검색
    private String campName;
    private String nickName;

    private String environment;
    private String addr1;
    private String addr2;
    private String latitude;
    private String longitude;

    private LocalDateTime createDate;
    // 검색 필터(UNAUTHORIZED or AUTHORIZED)
    private String campAuthStatus;
}
