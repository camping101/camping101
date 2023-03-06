package com.camping101.beta.reservation.repository;

import com.camping101.beta.reservation.entity.Reservation;
import com.camping101.beta.site.entity.Site;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


//    @Query("select r from Reservation r where r.createdAt <=: createDateTime")
//    List<Reservation> findByMemberAndCreatedAt(Member member,
//        @Param("createDateTime") LocalDateTime createDateTime);
//
//    List<Reservation> findByMemberAndCreatedAtBetween(LocalDateTime createdAt, LocalDateTime );

    List<Reservation> findBySite(Site site);

//    Long campId, String siteName
//    List<Reservation> findByCamp


}
