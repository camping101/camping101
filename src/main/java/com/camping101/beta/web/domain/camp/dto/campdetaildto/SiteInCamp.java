package com.camping101.beta.web.domain.camp.dto.campdetaildto;

import com.camping101.beta.db.entity.site.SiteCapacity;
import com.camping101.beta.db.entity.site.SiteType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SiteInCamp {

    private Long siteId;
//    private Long campId;
    private String name;
    private String rpImage; //대표이미지
    private String introduction;
    private SiteType type; // 사이트 타입( 글램핑 등...)
    private boolean openYn; // 노출 상태, 비노출 상태
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int leastScheduling;
    private SiteCapacity siteCapacity;
    private int price;


}
