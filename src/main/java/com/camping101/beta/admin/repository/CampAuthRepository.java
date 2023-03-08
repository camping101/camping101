package com.camping101.beta.admin.repository;

import com.camping101.beta.admin.entity.CampAuth;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampAuthRepository extends JpaRepository<CampAuth, Long> {

    @Query("select c from CampAuth c join fetch c.camp where c.campAuthId =:campAuthId")
    Optional<CampAuth> findByCampAuthId11(@Param("campAuthId") Long campAuthId);



}
