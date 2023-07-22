package com.camping101.beta.web.domain.camplog.repository;

import com.camping101.beta.db.entity.camplog.CampLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampLogRepository extends JpaRepository<CampLog, Long> {

}
