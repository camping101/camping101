package com.camping101.beta.db.entity.reservation;

import static com.camping101.beta.db.entity.reservation.ReservationStatus.CANCEL;
import static com.camping101.beta.db.entity.reservation.ReservationStatus.COMP;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.web.domain.reservation.dto.ReservationCreateRequest;
import com.camping101.beta.web.domain.reservation.dto.ReservationCreateResponse;
import com.camping101.beta.web.domain.reservation.dto.ReservationDetailsResponse;
import com.camping101.beta.web.domain.reservation.dto.ReservationListResponse;
import com.camping101.beta.db.entity.site.Site;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int humanCapacity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private int payment;

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime cancelAt;

    private boolean campLogYn; // 캠프로그 여부
    private boolean campLogWritableYn; // 캠프로그 쓰기 권한

    public void addMember(Member member) {
        this.member = member;
    }

    public void addSite(Site site) {
        this.site = site;
    }

    public long addPayment(int price, LocalDateTime startDate, LocalDateTime endDate) {

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return price * days;

    }

    public static void modifyReservationStatus(Reservation reservation) {

        reservation.status = CANCEL;
    }


    public static Reservation toEntity(ReservationCreateRequest reservationCreateRequest) {

        return Reservation.builder()
            .startDate(reservationCreateRequest.getStartDate())
            .endDate(reservationCreateRequest.getEndDate())
            .humanCapacity(reservationCreateRequest.getHumanCapacity())
            .status(COMP)
            .payment(reservationCreateRequest.getPayment())
            .campLogYn(false)
            .campLogWritableYn(true)
            .build();

    }

    public static ReservationListResponse toReservationListResponse(Reservation reservation) {

        return ReservationListResponse.builder()
            .memberId(reservation.getMember().getMemberId())
            .reservationId(reservation.getReservationId())
            .siteId(reservation.getSite().getSiteId())
            .siteName(reservation.getSite().getName())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .humanCapacity(reservation.getHumanCapacity())
            .status(reservation.getStatus())
            .payment(reservation.getPayment())
            .createdAt(reservation.getCreatedAt())
            .cancelAt(reservation.getCancelAt())
            .build();

    }

    public static ReservationCreateResponse toReservationCreateResponse(Reservation reservation) {

        return ReservationCreateResponse.builder()
            .memberId(reservation.getMember().getMemberId())
            .reservationId(reservation.getReservationId())
            .siteId(reservation.getSite().getSiteId())
            .siteName(reservation.getSite().getName())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .humanCapacity(reservation.getHumanCapacity())
            .status(reservation.getStatus())
            .payment(reservation.getPayment())
            .createdAt(reservation.getCreatedAt())
            .campLogYn(reservation.isCampLogYn())
            .campLogWritableYn(reservation.isCampLogWritableYn())
            .build();

    }

    public static ReservationDetailsResponse toReservationDetailsResponse(Reservation reservation) {

        if(reservation.getStatus() == CANCEL) {

            return ReservationDetailsResponse.builder()
                .memberId(reservation.getMember().getMemberId())
                .reservationId(reservation.getReservationId())
                .siteId(reservation.getSite().getSiteId())
                .siteName(reservation.getSite().getName())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .humanCapacity(reservation.getHumanCapacity())
                .status(reservation.getStatus())
                .payment(reservation.getPayment())
                .cancelAt(reservation.getCancelAt())
                .campLogYn(reservation.isCampLogYn())
                .campLogWritableYn(reservation.isCampLogWritableYn())
                .build();

        } else {

            return ReservationDetailsResponse.builder()
                .memberId(reservation.getMember().getMemberId())
                .reservationId(reservation.getReservationId())
                .siteId(reservation.getSite().getSiteId())
                .siteName(reservation.getSite().getName())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .humanCapacity(reservation.getHumanCapacity())
                .status(reservation.getStatus())
                .payment(reservation.getPayment())
                .createdAt(reservation.getCreatedAt())
                .campLogYn(reservation.isCampLogYn())
                .campLogWritableYn(reservation.isCampLogWritableYn())
                .build();
        }

    }


    public void changeCampLogWritableYn(Reservation reservation) {

        this.campLogWritableYn = true;

    }
}
