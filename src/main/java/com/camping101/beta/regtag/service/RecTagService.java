package com.camping101.beta.regtag.service;

import com.camping101.beta.regtag.dto.RecTagListRequest;
import com.camping101.beta.regtag.dto.RecTagListResponse;
import com.camping101.beta.regtag.entity.RecTag;
import com.camping101.beta.regtag.repository.RecTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecTagService {

    private final RecTagRepository recTagRepository;

    public RecTagListResponse getAllRecTags(RecTagListRequest request){

        Pageable page = PageRequest.of(request.getPageNumber(), request.getRecordSize(), Sort.Direction.DESC, "createdAt");
        Page<RecTag> rectags = recTagRepository.findAll(page);

        return RecTagListResponse.fromEntity(rectags);
    }

}
