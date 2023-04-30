package com.camping101.beta.web.domain.site.service;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.global.exception.CannotFindSiteException;
import com.camping101.beta.web.domain.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindSiteService {

    private final SiteRepository siteRepository;

    public Site findSiteOrElseThrow(Long siteId) {

        return siteRepository.findById(siteId).orElseThrow(CannotFindSiteException::new);


    }

}
