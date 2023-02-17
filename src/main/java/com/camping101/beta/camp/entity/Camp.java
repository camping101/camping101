package com.camping101.beta.camp.entity;

import static javax.persistence.EnumType.STRING;

import com.camping101.beta.camp.dto.CampCreateRequest;
import com.camping101.beta.camp.dto.CampCreateResponse;
import com.camping101.beta.camp.dto.CampListResponse;
import com.camping101.beta.camp.dto.CampModifyRequest;
import com.camping101.beta.camp.dto.CampModifyResponse;
import com.camping101.beta.member.entity.Member;
import java.time.LocalDateTime;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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


    public static Camp toEntity(CampCreateRequest campCreateRequest) {

        return Camp.builder()
            .intro(campCreateRequest.getIntro())
            .manageStatus(campCreateRequest.getManageStatus())
            .location(campCreateRequest.getLocation())
            .tel(campCreateRequest.getTel())
            .oneLineReserveYn(campCreateRequest.getOneLineReserveYn())
            .openSeason(campCreateRequest.getOpenSeason())
            .openDateOfWeek(campCreateRequest.getOpenDateOfWeek())
            .facilityCnt(campCreateRequest.getFacilityCnt())
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

    public static CampListResponse toCampListResponse(Camp camp) {

        return CampListResponse.builder()
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

    public void addMember(Member member) {

        this.member = member;

    }
}