package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.TemporalPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TemporalPasswordRepository extends JpaRepository<TemporalPassword, Long> {

    @Query("select t from TemporalPassword t join fetch t.member where t.member.memberId = :memberId")
    Optional<TemporalPassword> findByMemberId(Long memberId);

    Optional<TemporalPassword> findByMember(Member member);

}
