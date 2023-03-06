package com.camping101.beta.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CampAuthListSearchResponse {

    private Long campId;
    private String campName;
    private String location;
    private String tel;
    private String email;
    private LocalDateTime createDate;
    private String campAuthStatus;

    @QueryProjection
    public CampAuthListSearchResponse(Long campId, String campName, String location, String tel,
        String email, LocalDateTime createDate, String campAuthStatus) {
        this.campId = campId;
        this.campName = campName;
        this.location = location;
        this.tel = tel;
        this.email = email;
        this.createDate = createDate;
        this.campAuthStatus = campAuthStatus;
    }
}
