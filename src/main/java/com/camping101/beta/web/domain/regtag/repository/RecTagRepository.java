package com.camping101.beta.web.domain.regtag.repository;

import com.camping101.beta.db.entity.regtag.RecTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecTagRepository extends JpaRepository<RecTag, Long> {

    Optional<RecTag> findByName(String name);
    Page<RecTag> findByUseYnIs(boolean useYn, Pageable pageable);

}
