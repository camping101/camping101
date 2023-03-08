package com.camping101.beta.admin.campAuth.dto;

import com.camping101.beta.admin.campAuth.status.CampAuthStatus;
import com.camping101.beta.camp.entity.Location;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CampAuthListSearchResponse {

    private Long campId;
    private String campName;
    private Location location;
    private String tel;
    private String email;
    private LocalDateTime createAt;
    private String campAuthStatus;

    @QueryProjection
    public CampAuthListSearchResponse(Long campId, String campName, Location location, String tel,
        String email, LocalDateTime createAt, CampAuthStatus campAuthStatus) {
        this.campId = campId;
        this.campName = campName;
        this.location = location;
        this.tel = tel;
        this.email = email;
        this.createAt = createAt;
        this.campAuthStatus = String.valueOf(campAuthStatus);
    }
}
