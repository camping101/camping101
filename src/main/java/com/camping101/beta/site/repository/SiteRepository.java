package com.camping101.beta.site.repository;

import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.site.entity.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    Page<Site> findAllByCamp(Camp camp, Pageable pageable);


}
