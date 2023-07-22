package com.camping101.beta.db.entity.reservation;

import static com.camping101.beta.db.entity.reservation.ReservationStatus.CANCEL;
import static org.assertj.core.api.Assertions.assertThat;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.site.Site;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("예약 엔티티에 회원 엔티티를 넣을 수 있습니다.")
    void addMemberTest() {
        // given
        Reservation reservation = new Reservation();
        Member member = new Member();
        // when
        reservation.addMember(member);
        // then
        assertThat(reservation.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("예약 엔티티에 사이트 엔티티를 넣을 수 있습니다. 만약 사이트 엔티티에 해당 예약 정보가 없다면 추가합니다")
    void addSiteTest() {
        // given
        Reservation reservation = new Reservation();
        Site site = new Site();
        // when
        reservation.addSite(site);
        // then
        assertThat(reservation.getSite()).isEqualTo(site);
        assertThat(site.getReservationList().contains(reservation)).isTrue();
    }

    @Test
    @DisplayName("예약을 취소할 수 있습니다.")
    void modifyReservationStatusTest() {
        // given
        Reservation reservation = new Reservation();
        // when
        reservation.modifyReservationStatus();
        // then
        assertThat(reservation.getStatus()).isEqualTo(CANCEL);
        assertThat(reservation.getCancelAt()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("예약을 해서 캠프로그를 작성할 수 있습니다.")
    void changeCampLogWritableYnTest() {
        // given
        Reservation reservation = new Reservation();
        // when
        reservation.changeCampLogWritableYn();
        // then
        assertThat(reservation.isCampLogWritableYn()).isTrue();
    }

    @Test
    @DisplayName("예약엔티티에 총 금액을 저장할 수 있습니다.")
    void addPaymentTest(){
        // given
        Reservation reservation = new Reservation();
        Long payment = 30000L;
        // when
        reservation.addPayment(payment);
        // then
        assertThat(reservation.getPayment()).isEqualTo(30000L);
    }
}