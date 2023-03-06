package com.camping101.beta.regtag.repository;

import com.camping101.beta.regtag.entity.RecTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecTagRepository extends JpaRepository<RecTag, Long> {

    Optional<RecTag> findByName(String name);
    Page<RecTag> findByUseYnIs(boolean useYn, Pageable pageable);

}
