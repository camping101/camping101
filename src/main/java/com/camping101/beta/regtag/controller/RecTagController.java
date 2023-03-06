package com.camping101.beta.regtag.controller;

import com.camping101.beta.regtag.dto.RecTagListRequest;
import com.camping101.beta.regtag.dto.RecTagListResponse;
import com.camping101.beta.regtag.service.RecTagService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"추천태그 조회 API (비회원 가능)"})
@RequestMapping("/api/camplog/regtag")
public class RecTagController {

    private final RecTagService recTagService;

    @GetMapping
    public ResponseEntity<RecTagListResponse> getAllRecTags(@RequestBody RecTagListRequest request){

        var recTags = recTagService.getAllRecTags(request);

        return ResponseEntity.ok(recTags);
    }

}
