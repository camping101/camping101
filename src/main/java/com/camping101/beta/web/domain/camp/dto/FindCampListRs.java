package com.camping101.beta.web.domain.camp.dto;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.camp.Location;
import com.camping101.beta.db.entity.camp.ManageStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindCampListRs {

    private String campName;
    private Long campId;
    private String intro;
    private ManageStatus manageStatus;
    private Location location;
    private String openSeason;
    private String animalCapable;
    private String firstImage;
    private Long campLogCnt;

    public static FindCampListRs createCampListRs(Camp camp) {

        return FindCampListRs.builder()
            .campId(camp.getCampId())
            .campName(camp.getName())
            .intro(camp.getIntro())
            .manageStatus(camp.getManageStatus())
            .location(camp.getLocation())
            .openSeason(camp.getOpenSeason())
            .animalCapable(camp.getAnimalCapable())
            .firstImage(camp.getFirstImage())
            .campLogCnt(camp.getCampLogCnt())
            .build();
    }

    @QueryProjection
    public FindCampListRs(String campName, Long campId, String intro, ManageStatus manageStatus,
        Location location, String openSeason, String animalCapable, String firstImage,
        Long campLogCnt) {
        this.campName = campName;
        this.campId = campId;
        this.intro = intro;
        this.manageStatus = manageStatus;
        this.location = location;
        this.openSeason = openSeason;
        this.animalCapable = animalCapable;
        this.firstImage = firstImage;
        this.campLogCnt = campLogCnt;
    }
}
