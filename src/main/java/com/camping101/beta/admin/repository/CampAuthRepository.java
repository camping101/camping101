package com.camping101.beta.admin.repository;

import com.camping101.beta.admin.entity.CampAuth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampAuthRepository extends JpaRepository<CampAuth, Long> {



}
