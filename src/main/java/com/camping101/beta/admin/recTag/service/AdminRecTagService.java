package com.camping101.beta.admin.recTag.service;

import com.camping101.beta.admin.recTag.dto.AdminRecTagCreateRequest;
import com.camping101.beta.regtag.dto.RecTagListRequest;
import com.camping101.beta.regtag.dto.RecTagListResponse;
import com.camping101.beta.regtag.entity.RecTag;
import com.camping101.beta.regtag.repository.RecTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminRecTagService {

    private final RecTagRepository recTagRepository;

    public void createRecTag(AdminRecTagCreateRequest request){

        var optionalRecTag = recTagRepository.findByName(request.getName());

        if (optionalRecTag.isPresent()) {
            throw new RuntimeException("");
        }

        recTagRepository.save(RecTag.from(request));

    }

    public RecTagListResponse getAllRecTags(RecTagListRequest request){

        var page = PageRequest.of(request.getPageNumber(), request.getRecordSize(),
                                   Sort.Direction.DESC, "created_at");

        Page<RecTag> rectags;
        if (Objects.isNull(request.getUserYn())) {
            rectags = recTagRepository.findAll(page);
        } else {
            rectags = recTagRepository.findByUseYnIs(request.getUserYn(), page);
        }
        return RecTagListResponse.fromEntity(rectags);
    }

    @Transactional
    public void activateOrDeactivateRecTag(String name, boolean useYn){

        var recTag = recTagRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException(""));

        recTag.setUseYn(useYn);

    }

    @Transactional
    public void deleteRecTag(Long recTagId){

        var recTag = recTagRepository.findById(recTagId)
                .orElseThrow(() -> new RuntimeException(""));

        recTagRepository.delete(recTag);

    }

}
