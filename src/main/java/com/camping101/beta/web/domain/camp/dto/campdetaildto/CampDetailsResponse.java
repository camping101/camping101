package com.camping101.beta.web.domain.camp.dto.campdetaildto;

import com.camping101.beta.db.entity.camp.FacilityCnt;
import com.camping101.beta.db.entity.camp.Location;
import com.camping101.beta.db.entity.camp.ManageStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CampDetailsResponse {

    private Long campId;
    private String name;
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
    private String businessNo;
    private List<SiteInCamp> siteInCampLists = new ArrayList<>();
    private List<CampLogInCamp> campLogInCampLists = new ArrayList<>();


    public CampDetailsResponse(Long campId, String name, String intro, ManageStatus manageStatus,
        Location location, String tel, String oneLineReserveYn, String openSeason,
        LocalDateTime openDateOfWeek, FacilityCnt facilityCnt, String facility, String leisure,
        String animalCapable, String equipmentTools, String firstImage, String homepage,
        String businessNo) {
        this.campId = campId;
        this.name = name;
        this.intro = intro;
        this.manageStatus = manageStatus;
        this.location = location;
        this.tel = tel;
        this.oneLineReserveYn = oneLineReserveYn;
        this.openSeason = openSeason;
        this.openDateOfWeek = openDateOfWeek;
        this.facilityCnt = facilityCnt;
        this.facility = facility;
        this.leisure = leisure;
        this.animalCapable = animalCapable;
        this.equipmentTools = equipmentTools;
        this.firstImage = firstImage;
        this.homepage = homepage;
        this.businessNo = businessNo;
    }

    public void addSiteInCamp(List<SiteInCamp> sites) {
        this.siteInCampLists = sites;
    }

    public void addCampLogInCamp(List<CampLogInCamp> campLogs) {
        this.campLogInCampLists = campLogs;
    }
}
