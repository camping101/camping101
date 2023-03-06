package com.camping101.beta.camp.dto;

import com.camping101.beta.camp.entity.FacilityCnt;
import com.camping101.beta.camp.entity.Location;
import com.camping101.beta.camp.entity.ManageStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CampListResponse {

    private Long memberId;
    private String campName;
    private Long campId;
    private String intro;
    private ManageStatus manageStatus;
    private Location location;
    private String openSeason;
    private String animalCapable;
    private String firstImage;

}
