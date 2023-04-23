package com.camping101.beta.web.domain.siteseat.service;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.entity.siteseat.SiteSeat;
import com.camping101.beta.web.domain.site.service.SiteService;
import com.camping101.beta.web.domain.siteseat.repository.SiteSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteSeatService {

    private final SiteSeatRepository siteSeatRepository;
    private final SiteService siteService;

    public void createSiteSeat(String seatNumber, Integer humanCapacity, Long siteId) {

        SiteSeat siteSeat = SiteSeat.createSiteSeat(seatNumber, humanCapacity);

        siteSeatRepository.save(siteSeat);

        Site site = siteService.findSiteOrElseThrow(siteId);

        site.addSiteSeat(siteSeat);
    }

    public void deleteSiteSeat(Long siteSeatId) {

        siteSeatRepository.deleteById(siteSeatId);

    }

}
