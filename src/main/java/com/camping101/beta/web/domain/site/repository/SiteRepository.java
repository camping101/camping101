package com.camping101.beta.web.domain.site.repository;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.entity.site.SiteStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    List<Site> findAllByCampAndOpenYn(Camp camp, boolean openYn, SiteStatus AVAILABLE);

    // 캠핑장의 모든 사이트 가져오기
    List<Site> findByCamp(Camp camp);


}
