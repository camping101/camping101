package com.camping101.beta.camp.repository;

import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampRepository extends JpaRepository<Camp, Long> {

    // 캠핑장 목록 조회(회원, 비회원)
    Page<Camp> findAll(Pageable pageable);

    // 자신의 캠핑장 목록 조회(주인)
    Page<Camp> findAllByMember(Pageable pageable, Member member);


}
