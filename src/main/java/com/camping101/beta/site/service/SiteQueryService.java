package com.camping101.beta.site.service;

import static com.camping101.beta.campLog.entity.QCampLog.campLog;
import static com.camping101.beta.member.entity.QMember.member;
import static com.camping101.beta.reservation.entity.QReservation.reservation;
import static com.camping101.beta.site.entity.QSite.site;

import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.repository.CampRepository;
import com.camping101.beta.reservation.dto.ReservationOwnerListResponse;
import com.camping101.beta.reservation.entity.Reservation;
import com.camping101.beta.reservation.repository.ReservationRepository;
import com.camping101.beta.site.dto.sitedetailsresponse.SiteDetailsResponse;
import com.camping101.beta.site.entity.Site;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SiteQueryService {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public SiteQueryService(EntityManager em, CampRepository campRepository,
        ReservationRepository reservationRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    //     사이트 상세 조회
//     1. 회원(손님)은 사이트 상세 정보를 확인할 수 있다.
//    - 테이블의 모든 정보
    //- 예약 정보
//    - 캠프 로그
    @Transactional
    public SiteDetailsResponse findSiteDetails(Long siteId, Long memberId) {

        // 여기서 fetch 조인을 사용해 쿼리 한방으로 해결하려 했으나, fetch 조인은 on 절을 사용할 수 없다.
        // => where 절은 사용할 수 있다.

        Site findSite = queryFactory.select(site).from(site)
            .innerJoin(site.campLogList, campLog).fetchJoin()
//            .innerJoin(site.reservationList, reservation).fetchJoin()
            .where(site.siteId.eq(siteId))
            .distinct()
            .fetchOne();

        List<Reservation> reservations = queryFactory.select(reservation).from(reservation)
            .innerJoin(reservation.member, member)
            .on(member.memberId.eq(memberId))
            .fetch();

        findSite.addReservation(reservations);

        return new SiteDetailsResponse(findSite);
    }


    //     사이트 상세 조회
//     1. 주인(캠핑장 사장) 은 사이트 상세 정보를 확인할 수 있다.
//    - 테이블의 모든 정보
//    - 캠프 로그
    @Transactional
    public SiteDetailsResponse findSiteDetails(Long siteId) {

        Site findSite = queryFactory.select(site).from(site)
            .leftJoin(site.campLogList, campLog).fetchJoin()
            .where(site.siteId.eq(siteId))
            .distinct()
            .fetchOne();

        return new SiteDetailsResponse(findSite);
    }




}
