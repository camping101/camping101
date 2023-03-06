package com.camping101.beta.member.repository;

import com.camping101.beta.member.entity.TemporalPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemporalPasswordRepository extends JpaRepository<TemporalPassword, Long> {

    Optional<TemporalPassword> findByEmail(String email);

}
