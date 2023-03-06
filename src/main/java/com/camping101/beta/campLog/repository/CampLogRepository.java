package com.camping101.beta.campLog.repository;

import com.camping101.beta.campLog.entity.CampLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampLogRepository extends JpaRepository<CampLog, Long> {


}
