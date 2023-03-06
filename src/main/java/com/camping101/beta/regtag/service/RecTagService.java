package com.camping101.beta.regtag.service;

import com.camping101.beta.regtag.dto.RecTagListRequest;
import com.camping101.beta.regtag.dto.RecTagListResponse;
import com.camping101.beta.regtag.repository.RecTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecTagService {

    private final RecTagRepository recTagRepository;

    public RecTagListResponse getAllRecTags(RecTagListRequest request){

        var page = PageRequest.of(request.getPageNumber(), request.getRecordSize(), Sort.Direction.DESC, "created_at");
        var rectags = recTagRepository.findAll(page);

        return RecTagListResponse.fromEntity(rectags);
    }

}
