package com.camping101.beta.web.domain.camp.dto;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.camp.FacilityCnt;
import com.camping101.beta.db.entity.camp.Location;
import com.camping101.beta.db.entity.camp.ManageStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindCampDetailsOwnerRs {

    private Long campId;
    private String campName;
    private String intro;
    private ManageStatus manageStatus;
    private Location location;

    private String tel;
    private String oneLineReserveYn;

    private String openSeason;
    private LocalDate openDateOfWeek;
    private FacilityCnt facilityCnt;

    private String facility;
    private String leisure;
    private String animalCapable;
    private String equipmentTools;
    private String firstImage;
    private String homepage;
    private String businessNo; // 사업자 번호

    public static FindCampDetailsOwnerRs createFindCampDetailsOwnerRs(Camp camp) {

        return FindCampDetailsOwnerRs.builder()
            .campId(camp.getCampId())
            .campName(camp.getName())
            .intro(camp.getIntro())
            .manageStatus(camp.getManageStatus())
            .location(camp.getLocation())
            .tel(camp.getTel())
            .oneLineReserveYn(camp.getOneLineReserveYn())
            .openSeason(camp.getOpenSeason())
            .openDateOfWeek(camp.getOpenDateOfWeek())
            .facilityCnt(camp.getFacilityCnt())
            .facility(camp.getFacility())
            .leisure(camp.getLeisure())
            .animalCapable(camp.getAnimalCapable())
            .equipmentTools(camp.getEquipmentTools())
            .firstImage(camp.getFirstImage())
            .homepage(camp.getHomepage())
            .businessNo(camp.getBusinessNo())
            .build();
    }

}
