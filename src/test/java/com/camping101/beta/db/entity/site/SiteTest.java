package com.camping101.beta.db.entity.site;

import static org.assertj.core.api.Assertions.assertThat;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.reservation.Reservation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SiteTest {

    @Test
    @DisplayName("사이트 엔티티에 캠핑장 엔티티를 저장할 수 있습니다. 캠핑장 엔티티에 사이트가 등록되어 있지 않다면 추가합니다.")
    void addCampTest() {
        // given
        Site site = new Site();
        Camp camp = new Camp();
        // when
        site.addCamp(camp);
        // then
        assertThat(site.getCamp()).isEqualTo(camp);
        assertThat(camp.getSites().contains(site)).isTrue();
    }

    @Test
    @DisplayName("사이트 엔티티에 예약 엔티티를 저장할 수 있습니다.")
    void addReservationTest() {
        // given
        Site site = new Site();
        Reservation reservation = new Reservation();
        // when
        site.addReservation(reservation);
        // then
        assertThat(site.getReservationList().contains(reservation)).isTrue();
    }

    @Test
    @DisplayName("사이트의 개장 여부를 변경할 수 있습니다.")
    void changeOpenYnTest() {
        // given
        Site closedSite = new Site();
        Site openedSite = openedSite();
        // when
        closedSite.changeOpenYn();
        openedSite.changeOpenYn();
        // then
        assertThat(closedSite.isOpenYn()).isTrue();
        assertThat(openedSite.isOpenYn()).isFalse();
    }

    private static Site openedSite() {
        Site openSite = new Site();
        openSite.changeOpenYn();
        return openSite;
    }
}