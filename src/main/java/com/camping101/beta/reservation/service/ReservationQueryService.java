package com.camping101.beta.reservation.service;

import static com.camping101.beta.reservation.entity.QReservation.reservation;
import static java.time.LocalDateTime.now;

import com.camping101.beta.reservation.entity.QReservation;
import com.camping101.beta.reservation.entity.Reservation;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationQueryService {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ReservationQueryService(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 해당 기간에 해당하는 모든 회원의 예약 내역 가져오기
    @Transactional(readOnly = true)
    public List<Reservation> findReservationList(int month, Long memberId) {

        DateTemplate<LocalDateTime> dateFormat = Expressions.dateTemplate(
            LocalDateTime.class, "DATE_SUB({0},{1})",
            now(), "INTERVAL -" + month + " MONTH");

        return queryFactory.select(reservation)
            .from(reservation)
            .where(
                // 해당 기간 내의 예약들(기간 필터를 사용)
                reservation.createdAt.goe(dateFormat)
//                    .and(reservation.createdAt.loe(now())) => 이거 왜 있던 거지?
                    // 해당 회원이 한 예약들
                    .and(reservation.member.memberId.eq(memberId))
            )
            .fetch();
    }

    // 예약 가능 날 확인(여기에 취소된 예약 날은 제외하고 해야하는데...)
    // 해당 값은 자동적으로 LocalDateTime.now() 보다 큰 값들이다.
    public List<Reservation> findReservationBySite(Long siteId) {

        return queryFactory.select(reservation).from(reservation)
            .where(reservation.endDate.lt(LocalDateTime.now()))
            .fetch();
    }

}
