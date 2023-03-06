package com.camping101.beta.camp.dto;

import com.camping101.beta.camp.entity.Location;
import com.camping101.beta.camp.entity.ManageStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CampCreateResponse {

    private Long campId;
    private String campName;
    private String intro;
    private String manageStatus;
    private String environment;
    private String addr1;
    private String addr2;
    private String latitude;
    private String longitude;

    private String tel;
    private String oneLineReserveYn;

    private String openSeason;
    private LocalDateTime openDateOfWeek;
    private int toiletCnt;
    private int showerCnt;
    private int waterProofCnt;
    private String facility;
    private String leisure;
    private String animalCapable;
    private String equipmentTools;
    private String firstImage;
    private String homepage;
    private String businessNo; // 사업자 번호

}
