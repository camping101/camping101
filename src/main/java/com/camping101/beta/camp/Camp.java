package com.camping101.beta.camp;

import static javax.persistence.EnumType.STRING;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Camp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_id")
    private Long campId;

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
    private String reservePage; // 데이터 확인하기
    private String businessNo;



























}
