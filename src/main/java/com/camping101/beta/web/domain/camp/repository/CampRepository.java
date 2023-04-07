package com.camping101.beta.web.domain.camp.repository;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.camp.ManageStatus;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.site.Site;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CampRepository extends JpaRepository<Camp, Long> {

    // 캠핑장 목록 조회(회원, 비회원)
    Page<Camp> findAllByManageStatus(Pageable pageable, ManageStatus manageStatus);

    // 자신의 캠핑장 목록 조회(주인)
    Page<Camp> findAllByMemberAndManageStatus(Pageable pageable, Member member, ManageStatus manageStatus);

    @Query("select c.sites from Camp c where c.campId =: campId")
    List<Site> findSitesByCampId(Long campId);

}
