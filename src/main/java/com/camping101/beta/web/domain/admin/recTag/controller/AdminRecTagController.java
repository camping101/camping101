package com.camping101.beta.web.domain.admin.recTag.controller;

import com.camping101.beta.web.domain.admin.recTag.dto.AdminRecTagCreateRequest;
import com.camping101.beta.web.domain.admin.recTag.service.AdminRecTagService;
import com.camping101.beta.web.domain.regtag.dto.RecTagListRequest;
import com.camping101.beta.web.domain.regtag.dto.RecTagListResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/rectag")
@Api(tags = "관리자 추천 태그 API")
public class AdminRecTagController {

    private final AdminRecTagService adminRecTagService;

    @PostMapping
    public ResponseEntity<?> createRecTag(@RequestBody AdminRecTagCreateRequest request){

        adminRecTagService.createRecTag(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<RecTagListResponse> getAllRecTags(RecTagListRequest request){

        RecTagListResponse recTags = adminRecTagService.getAllRecTags(request);

        return ResponseEntity.ok(recTags);
    }

    @PatchMapping
    public ResponseEntity<?> activateOrDeactivateRecTag(@RequestParam String name,
                                                        @RequestParam boolean userYn){

        adminRecTagService.activateOrDeactivateRecTag(name, userYn);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recTagId}")
    public ResponseEntity<?> deleteRecTag(@PathVariable Long recTagId){

        adminRecTagService.deleteRecTag(recTagId);

        return ResponseEntity.ok().build();
    }

}
