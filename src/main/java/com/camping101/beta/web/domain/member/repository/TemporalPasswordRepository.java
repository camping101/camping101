package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.TemporalPassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporalPasswordRepository extends CrudRepository<TemporalPassword, String> {

    Optional<TemporalPassword> findByMemberId(Long memberId);

}
