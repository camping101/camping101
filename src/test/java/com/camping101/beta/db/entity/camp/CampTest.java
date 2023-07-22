package com.camping101.beta.db.entity.camp;

import static com.camping101.beta.db.entity.camp.ManageStatus.AUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.site.Site;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CampTest {

    @Test
    @DisplayName("캠핑장 엔티티에 주인을 지정할 수 있다.")
    void addMemberTest() {
        // given
        Member member = new Member();
        Camp camp = new Camp();
        // when
        camp.addMember(member);
        // then
        assertThat(camp.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("관리자는 캠핑장 권한 허가를 할 수 있다.")
    void editManageStatusTest() {
        // given
        Camp camp = new Camp();
        // when
        camp.editManageStatus();
        // then
        assertThat(camp.getManageStatus()).isEqualTo(AUTHORIZED);
    }

    @Test
    @DisplayName("캠핑장에 대한 캠프로그 수를 늘릴 수 있다.")
    void plusCampLogCntTest() {
        // given
        Camp camp = new Camp();
        Long nowCampLogCnt = camp.getCampLogCnt();
        // when
        camp.plusCampLogCnt();
        // then
        assertThat(camp.getCampLogCnt()).isEqualTo(nowCampLogCnt + 1);
    }

    @Test
    @DisplayName("캠핑장에 대한 캠프로그 수를 줄일 수 있다.")
    void minusCampLogCntTest() {
        // given
        Camp camp = new Camp();
        // when
        camp.minusCampLogCnt();
        // then
        assertThat(camp.getCampLogCnt()).isEqualTo(0);
    }

    @Test
    @DisplayName("캠핑장에 사이트를 추가할 수 있습니다.")
    void addSiteTest(){
        // given
        Camp camp = new Camp();
        Site site = new Site();
        // when
        camp.addSite(site);
        // then
        assertThat(camp.getSites().contains(site)).isTrue();
    }
}