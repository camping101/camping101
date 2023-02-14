package com.camping101.beta.site.entity;

import com.camping101.beta.camp.Camp;
import java.time.LocalDateTime;
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

    private String openYn; //
    private String name;
    private String rpImage; //대표이미지
    private String introduction;

    @Enumerated(EnumType.STRING)
    private SiteType type; //

    @Embedded
    private SiteYn siteYn;

    private int score;
    private LocalDateTime checkIn; //
    private LocalDateTime checkOut;//
    private int leastScheduling; //

    @Embedded
    private SiteCapacity siteCapacity;

    private String mapImage;
    private String policy;
    private int price;
    private LocalDateTime refundableDate;























}
