package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.TemporalPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemporalPasswordRepository extends JpaRepository<TemporalPassword, Long> {

    Optional<TemporalPassword> findByMemberId(Long memberId);

}
