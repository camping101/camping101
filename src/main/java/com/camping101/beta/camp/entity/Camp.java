package com.camping101.beta.camp.entity;

import static com.camping101.beta.camp.entity.ManageStatus.AUTHORIZED;
import static javax.persistence.EnumType.STRING;

import com.camping101.beta.camp.dto.CampCreateRequest;
import com.camping101.beta.camp.dto.CampCreateResponse;
import com.camping101.beta.camp.dto.CampDetailsAdminResponse;
import com.camping101.beta.camp.dto.CampDetailsOwnerResponse;
import com.camping101.beta.camp.dto.CampListResponse;
import com.camping101.beta.camp.dto.CampModifyRequest;
import com.camping101.beta.camp.dto.CampModifyResponse;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.site.entity.Site;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Camp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_id")
    private Long campId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String name;

    private String intro;

    @Enumerated(STRING)
    private ManageStatus manageStatus;

    @Embedded
    private Location location;

    private String tel;
    private String oneLineReserveYn; // 데이터 확인하기 (불리언으로 데이터를 줄것같지 않다.)

    private String openSeason;
    private LocalDateTime openDateOfWeek;

    @Embedded
    private FacilityCnt facilityCnt;

    private String facility;
    private String leisure;
    private String animalCapable;
    private String equipmentTools;
    private String firstImage;
    private String homepage;
    private String businessNo;

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "camp")
    private List<Site> sites = new ArrayList<>();


    public static Camp toEntity(CampCreateRequest campCreateRequest) {

        return Camp.builder()
            .name(campCreateRequest.getCampName())
            .intro(campCreateRequest.getIntro())
            .manageStatus(ManageStatus.UNAUTHORIZED)
            .location(new Location(campCreateRequest.getEnvironment(), campCreateRequest.getAddr1(),
                campCreateRequest.getAddr2(), campCreateRequest.getLatitude(),
                campCreateRequest.getLongitude()))
            .tel(campCreateRequest.getTel())
            .oneLineReserveYn(campCreateRequest.getOneLineReserveYn())
            .openSeason(campCreateRequest.getOpenSeason())
            .openDateOfWeek(campCreateRequest.getOpenDateOfWeek())
            .facilityCnt(new FacilityCnt(campCreateRequest.getToiletCnt(),
                campCreateRequest.getShowerCnt(), campCreateRequest.getWaterProofCnt()))
            .facility(campCreateRequest.getFacility())
            .leisure(campCreateRequest.getLeisure())
            .animalCapable(campCreateRequest.getAnimalCapable())
            .equipmentTools(campCreateRequest.getEquipmentTools())
            .firstImage(campCreateRequest.getFirstImage())
            .homepage(campCreateRequest.getHomepage())
            .businessNo(campCreateRequest.getBusinessNo())
            .build();

    }

    public static CampCreateResponse toCampCreateResponse(Camp camp) {

        return CampCreateResponse.builder()
            .campId(camp.getCampId())
            .campName(camp.getName())
            .intro(camp.getIntro())
            .manageStatus(String.valueOf(camp.getManageStatus())) // => 캠핑장 생성이 완료되었습니다. 관리자가 요청을 확인합니다.
            .environment(camp.getLocation().getEnvironment())
            .addr1(camp.getLocation().getAddr1())
            .addr2(camp.getLocation().getAddr2())
            .latitude(camp.getLocation().getLatitude())
            .longitude(camp.getLocation().getLongitude())
            .tel(camp.getTel())
            .oneLineReserveYn(camp.getOneLineReserveYn())
            .openSeason(camp.getOpenSeason())
            .openDateOfWeek(camp.getOpenDateOfWeek())
            .showerCnt(camp.getFacilityCnt().getShowerCnt())
            .toiletCnt(camp.getFacilityCnt().getToiletCnt())
            .waterProofCnt(camp.getFacilityCnt().getWaterProofCnt())
            .facility(camp.getFacility())
            .leisure(camp.getLeisure())
            .animalCapable(camp.getAnimalCapable())
            .equipmentTools(camp.getEquipmentTools())
            .firstImage(camp.getFirstImage())
            .homepage(camp.getHomepage())
            .businessNo(camp.getBusinessNo())
            .build();

    }


    public static CampListResponse toCampListResponse(Camp camp) {

        return CampListResponse.builder()
            .memberId(camp.getMember().getMemberId())
            .campId(camp.getCampId())
            .campName(camp.getName())
            .intro(camp.getIntro())
            .manageStatus(camp.getManageStatus())
            .location(camp.getLocation())
            .openSeason(camp.getOpenSeason())
            .animalCapable(camp.getAnimalCapable())
            .firstImage(camp.getFirstImage())
            .build();
    }

    public static CampDetailsOwnerResponse toCampDetailsOwnerResponse(Camp camp) {

        return CampDetailsOwnerResponse.builder()
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

    public Camp updateCamp(CampModifyRequest campModifyRequest) {

        this.campId = campModifyRequest.getCampId();
        this.intro = campModifyRequest.getIntro();
        this.manageStatus = campModifyRequest.getManageStatus();
        this.location = campModifyRequest.getLocation();
        this.tel = campModifyRequest.getTel();
        this.oneLineReserveYn = campModifyRequest.getOneLineReserveYn();
        this.openSeason = campModifyRequest.getOpenSeason();
        this.openDateOfWeek = campModifyRequest.getOpenDateOfWeek();
        this.facilityCnt = campModifyRequest.getFacilityCnt();
        this.facility = campModifyRequest.getFacility();
        this.leisure = campModifyRequest.getLeisure();
        this.animalCapable = campModifyRequest.getAnimalCapable();
        this.equipmentTools = campModifyRequest.getEquipmentTools();
        this.firstImage = campModifyRequest.getFirstImage();
        this.homepage = campModifyRequest.getHomepage();
        this.businessNo = campModifyRequest.getBusinessNo();

        return this;

    }

    public static CampModifyResponse toCampModifyResponse(Camp camp) {

        return CampModifyResponse.builder()
            .memberId(camp.getMember().getMemberId())
            .campId(camp.getCampId())
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

    public static CampDetailsAdminResponse toCampDetailsAdminResponse(Camp camp) {

        return CampDetailsAdminResponse.builder()
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

    public void addMember(Member member) {

        this.member = member;

    }

    public void editManageStatus(Camp camp) {
        camp.manageStatus = AUTHORIZED;

    }

}
