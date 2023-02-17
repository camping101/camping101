package com.camping101.beta.reservation.entity;

import static com.camping101.beta.reservation.entity.ReservationStatus.CANCEL;
import static com.camping101.beta.reservation.entity.ReservationStatus.COMP;

import com.camping101.beta.member.entity.Member;
import com.camping101.beta.reservation.dto.ReservationCreateRequest;
import com.camping101.beta.reservation.dto.ReservationCreateResponse;
import com.camping101.beta.reservation.dto.ReservationDetailsResponse;
import com.camping101.beta.reservation.dto.ReservationListResponse;
import com.camping101.beta.site.entity.Site;
import java.time.LocalDateTime;
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

    private String campLogYn; //
    private String campLogWritableYn; //

    public void addMember(Member member) {
        this.member = member;

    }

    public void addSite(Site site) {
        this.site = site;
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
            .campLogYn(reservationCreateRequest.getCampLogYn())
            .campLogWritableYn(reservationCreateRequest.getCampLogWritableYn())
            .build();

    }

    public static ReservationListResponse toReservationListResponse(Reservation reservation) {

        return ReservationListResponse.builder()
            .memberId(reservation.getMember().getMemberId())
            .reservationId(reservation.getReservationId())
            .siteId(reservation.getSite().getSiteId())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .humanCapacity(reservation.getHumanCapacity())
            .status(reservation.getStatus())
            .payment(reservation.getPayment())
            .createdAt(reservation.getCreatedAt())
            .cancelAt(reservation.getCancelAt())
            .campLogYn(reservation.getCampLogYn())
            .campLogWritableYn(reservation.getCampLogWritableYn())
            .build();

    }

    public static ReservationCreateResponse toReservationCreateResponse(Reservation reservation) {

        return ReservationCreateResponse.builder()
            .memberId(reservation.getMember().getMemberId())
            .reservationId(reservation.getReservationId())
            .siteId(reservation.getSite().getSiteId())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .humanCapacity(reservation.getHumanCapacity())
            .status(reservation.getStatus())
            .payment(reservation.getPayment())
            .createdAt(reservation.getCreatedAt())
            .campLogYn(reservation.getCampLogYn())
            .campLogWritableYn(reservation.getCampLogWritableYn())
            .build();

    }

    public static ReservationDetailsResponse toReservationDetailsResponse(Reservation reservation) {

        return ReservationDetailsResponse.builder()
            .memberId(reservation.getMember().getMemberId())
            .reservationId(reservation.getReservationId())
            .siteId(reservation.getSite().getSiteId())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .humanCapacity(reservation.getHumanCapacity())
            .status(reservation.getStatus())
            .payment(reservation.getPayment())
            .createdAt(reservation.getCreatedAt())
            .cancelAt(reservation.getCancelAt())
            .campLogYn(reservation.getCampLogYn())
            .campLogWritableYn(reservation.getCampLogWritableYn())
            .build();

    }


}
