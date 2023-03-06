package com.camping101.beta.camp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.camping101.beta.admin.entity.CampAuth;
import com.camping101.beta.admin.repository.CampAuthRepository;
import com.camping101.beta.camp.dto.CampCreateRequest;
import com.camping101.beta.camp.dto.CampCreateResponse;
import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.entity.FacilityCnt;
import com.camping101.beta.camp.entity.Location;
import com.camping101.beta.camp.entity.ManageStatus;
import com.camping101.beta.camp.repository.CampRepository;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.site.service.SiteService;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(value = false)
class CampServiceTest {

    @Autowired
    private CampRepository campRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CampQueryService campQueryService;
    @Autowired
    private CampAuthRepository campAuthRepository;
    @Autowired
    private SiteService siteService;

    @Test
    void registerCamp() {

        CampCreateRequest request = createCampCreateRequest();
        Camp camp = Camp.toEntity(request);
        campRepository.save(camp);

        Long memberId = request.getMemberId();
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 회원이 없습니다");
        });

        camp.addMember(findMember); // 변경 감지

        CampAuth campAuth = CampAuth.toEntity(camp);
        campAuthRepository.save(campAuth);

        CampCreateResponse response = Camp.toCampCreateResponse(camp);
        assertThat(response.getCampId()).isEqualTo(camp.getCampId());
        assertThat(response.getCampName()).isEqualTo(camp.getName());
        assertThat(response.getIntro()).isEqualTo(camp.getIntro());
        assertThat(response.getManageStatus()).isEqualTo(String.valueOf(camp.getManageStatus()));
        assertThat(response.getEnvironment()).isEqualTo(camp.getLocation().getEnvironment());
        assertThat(response.getAddr1()).isEqualTo(camp.getLocation().getAddr1());
        assertThat(response.getAddr2()).isEqualTo(camp.getLocation().getAddr2());
        assertThat(response.getLatitude()).isEqualTo(camp.getLocation().getLatitude());
        assertThat(response.getLongitude()).isEqualTo(camp.getLocation().getLongitude());
        assertThat(response.getTel()).isEqualTo(camp.getTel());
        assertThat(response.getOneLineReserveYn()).isEqualTo(camp.getOneLineReserveYn());
        assertThat(response.getOpenSeason()).isEqualTo(camp.getOpenSeason());
        assertThat(response.getOpenDateOfWeek()).isEqualTo(camp.getOpenDateOfWeek());
        assertThat(response.getToiletCnt()).isEqualTo(camp.getFacilityCnt().getToiletCnt());
        assertThat(response.getShowerCnt()).isEqualTo(camp.getFacilityCnt().getShowerCnt());
        assertThat(response.getWaterProofCnt()).isEqualTo(camp.getFacilityCnt().getWaterProofCnt());
        assertThat(response.getFacility()).isEqualTo(camp.getFacility());
        assertThat(response.getLeisure()).isEqualTo(camp.getLeisure());
        assertThat(response.getAnimalCapable()).isEqualTo(camp.getAnimalCapable());
        assertThat(response.getEnvironment()).isEqualTo(camp.getEquipmentTools());
        assertThat(response.getFirstImage()).isEqualTo(camp.getFirstImage());
        assertThat(response.getHomepage()).isEqualTo(camp.getHomepage());
        assertThat(response.getBusinessNo()).isEqualTo(camp.getBusinessNo());

    }

    private static CampCreateRequest createCampCreateRequest() {

        return CampCreateRequest.builder()
            .memberId(1L)
            .CampName("캠핑장")
            .intro("캠핑장입니다")
            .environment("바다")
            .addr1("수원시")
            .addr2("영통구")
            .latitude("100")
            .longitude("200")
            .toiletCnt(1)
            .showerCnt(2)
            .waterProofCnt(3)
            .facility("편의시설")
            .leisure("여가시설")
            .animalCapable("가능")
            .equipmentTools("제공도구")
            .firstImage("1111")
            .homepage("camping101.com")
            .businessNo("111")
            .build();
    }

    @Test
    void findOwnerCampList() {
    }

    @Test
    void findCampList() {
    }

    @Test
    void findCampDetails() {
    }

    @Test
    void findCampDetailsOwner() {
    }

    @Test
    void findCampDetailsAdmin() {
    }

    @Test
    void modifyCamp() {
    }

    @Test
    void removeCamp() {
    }
}