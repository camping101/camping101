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
public class CampModifyRequest {

    private Long campId;
    private String intro;
    private ManageStatus manageStatus;
    private Location location;

    private String tel;
    private String oneLineReserveYn;

    private String openSeason;
    private LocalDateTime openDateOfWeek;
    private FacilityCnt facilityCnt;

    private String facility;
    private String leisure;
    private String animalCapable;
    private String equipmentTools;
    private String firstImage;
    private String homepage;
    private String businessNo; // 사업자 번호


}
