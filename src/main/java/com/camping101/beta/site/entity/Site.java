package com.camping101.beta.site.entity;

import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.reservation.entity.Reservation;
import com.camping101.beta.site.dto.SiteCreateRequest;
import com.camping101.beta.site.dto.SiteCreateResponse;
import com.camping101.beta.site.dto.SiteListResponse;
import com.camping101.beta.site.dto.SiteModifyRequest;
import com.camping101.beta.site.dto.SiteModifyResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
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

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")
    private Long siteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;
    @OneToMany(mappedBy = "site")
    private List<Reservation> reservationList = new ArrayList<>();
    @OneToMany(mappedBy = "site")
    private List<CampLog> campLogList = new ArrayList<>();

    private String name;
    private String rpImage; //대표이미지
    private String introduction;

    @Enumerated(EnumType.STRING)
    private SiteType type;

    private boolean openYn; // 노출 상태, 비노출 상태

    @Embedded
    private SiteYn siteYn;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int leastScheduling;

    @Embedded
    private SiteCapacity siteCapacity;

    private String mapImage;
    private String policy;
    private int price;
    private LocalDateTime refundableDate;

    public void addCamp(Camp camp) {

        this.camp = camp;
    }

    public void addReservation(List<Reservation> reservations) {
        this.reservationList = reservations;
    }

    public static Site toEntity(SiteCreateRequest siteCreateRequest) {

        return Site.builder()
            .name(siteCreateRequest.getName())
            .rpImage(siteCreateRequest.getRpImage())
            .introduction(siteCreateRequest.getIntroduction())
            .type(siteCreateRequest.getType())
            .openYn(false)
            .siteYn(siteCreateRequest.getSiteYn())
            .checkIn(siteCreateRequest.getCheckIn())
            .checkOut(siteCreateRequest.getCheckOut())
            .leastScheduling(siteCreateRequest.getLeastScheduling())
            .siteCapacity(siteCreateRequest.getSiteCapacity())
            .mapImage(siteCreateRequest.getMapImage())
            .policy(siteCreateRequest.getPolicy())
            .price(siteCreateRequest.getPrice())
            .refundableDate(siteCreateRequest.getRefundableDate())
            .build();

    }

    public static SiteCreateResponse toSiteCreateResponse(Site site) {

        return SiteCreateResponse.builder()
            .siteId(site.getSiteId())
            .campId(site.getCamp().getCampId())
            .name(site.getName())
            .rpImage(site.getRpImage())
            .introduction(site.getIntroduction())
            .type(site.getType())
            .openYn(site.isOpenYn())
            .siteYn(site.getSiteYn())
            .checkIn(site.getCheckIn())
            .checkOut(site.getCheckOut())
            .leastScheduling(site.getLeastScheduling())
            .siteCapacity(site.getSiteCapacity())
            .mapImage(site.getMapImage())
            .policy(site.getPolicy())
            .price(site.getPrice())
            .refundableDate(site.getRefundableDate())
            .build();

    }

    public Site updateSite(SiteModifyRequest siteModifyRequest) {

        this.name = siteModifyRequest.getName();
        this.rpImage = siteModifyRequest.getRpImage(); //대표 이미지
        this.introduction = siteModifyRequest.getIntroduction();
        this.type = siteModifyRequest.getType();
        this.openYn = siteModifyRequest.isOpenYn();
        this.siteYn = siteModifyRequest.getSiteYn();
        this.checkIn = siteModifyRequest.getCheckIn();
        this.checkOut = siteModifyRequest.getCheckOut();
        this.leastScheduling = siteModifyRequest.getLeastScheduling();
        this.siteCapacity = siteModifyRequest.getSiteCapacity();
        this.mapImage = siteModifyRequest.getMapImage();
        this.policy = siteModifyRequest.getPolicy();
        this.price = siteModifyRequest.getPrice();
        this.refundableDate = siteModifyRequest.getRefundableDate();

        return this;
    }

    public static Site toEntity(SiteModifyRequest siteModifyRequest) {

        return Site.builder()
            .siteId(siteModifyRequest.getSiteId())
            .name(siteModifyRequest.getName())
            .rpImage(siteModifyRequest.getRpImage())
            .introduction(siteModifyRequest.getIntroduction())
            .type(siteModifyRequest.getType())
            .siteYn(siteModifyRequest.getSiteYn())
            .checkIn(siteModifyRequest.getCheckIn())
            .checkOut(siteModifyRequest.getCheckOut())
            .leastScheduling(siteModifyRequest.getLeastScheduling())
            .siteCapacity(siteModifyRequest.getSiteCapacity())
            .mapImage(siteModifyRequest.getMapImage())
            .policy(siteModifyRequest.getPolicy())
            .price(siteModifyRequest.getPrice())
            .refundableDate(siteModifyRequest.getRefundableDate())
            .build();

    }

    public static SiteModifyResponse toSiteModifyResponse(Site site) {

        return SiteModifyResponse.builder()
            .siteId(site.getSiteId())
            .campId(site.getCamp().getCampId())
            .name(site.getName())
            .rpImage(site.getRpImage())
            .introduction(site.getIntroduction())
            .type(site.getType())
            .openYn(site.isOpenYn())
            .siteYn(site.getSiteYn())
            .checkIn(site.getCheckIn())
            .checkOut(site.getCheckOut())
            .leastScheduling(site.getLeastScheduling())
            .siteCapacity(site.getSiteCapacity())
            .mapImage(site.getMapImage())
            .policy(site.getPolicy())
            .price(site.getPrice())
            .refundableDate(site.getRefundableDate())
            .build();

    }

    public static SiteListResponse toSiteListResponse(Site site) {

        return SiteListResponse.builder()
            .siteId(site.getSiteId())
            .campId(site.getCamp().getCampId())
            .name(site.getName())
            .rpImage(site.getRpImage())
            .checkIn(site.getCheckIn())
            .checkOut(site.getCheckOut())
            .leastScheduling(site.getLeastScheduling())
            .price(site.getPrice())
            .build();

    }

    public void changeOpenYn(Site site) {
        site.openYn = true;
    }


}
