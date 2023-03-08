package com.camping101.beta.admin.campAuth.dto;

import com.camping101.beta.camp.entity.Location;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampAuthSearchRequest {

    // 검색
    private String campName;
    private String nickName;
    private Location location;

    private LocalDateTime createDate;
    // 검색 필터(UNAUTHORIZED or AUTHORIZED)
    private String campAuthStatus;
}
