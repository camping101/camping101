package com.camping101.beta.site.repository;

import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.site.entity.Site;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    Page<Site> findAllByCampAndOpenYn(Camp camp, boolean openYn, Pageable pageable);

    // 캠핑장의 모든 사이트 가져오기
    List<Site> findByCamp(Camp camp);


}
