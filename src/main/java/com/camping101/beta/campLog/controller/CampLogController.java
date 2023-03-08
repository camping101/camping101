package com.camping101.beta.campLog.controller;

import com.camping101.beta.campLog.dto.*;
import com.camping101.beta.campLog.service.CampLogService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camplog")
@Api(tags = {"캠프 로그 API"})
public class CampLogController {

    private final CampLogService campLogService;

    @PostMapping
    public ResponseEntity<CampLogInfoResponse> createCampLog(@RequestBody CampLogCreateRequest request,
                                                             @ApiIgnore Principal principal){

        request.setWriterEmail(principal.getName());
        CampLogInfoResponse createdCampLog = campLogService.createCampLog(request);

        return ResponseEntity.ok(createdCampLog);
    }

    @GetMapping
    public ResponseEntity<CampLogListResponse> getCampLogList(CampLogListRequest request){

        CampLogListResponse campLogs = campLogService.getCampLogList(request);

        return ResponseEntity.ok(campLogs);
    }

    @GetMapping("/{campLogId}")
    public ResponseEntity<CampLogInfoResponse> getCampLogInfo(@PathVariable Long campLogId){

        CampLogInfoResponse campLog = campLogService.getCampLogInfo(campLogId);

        return ResponseEntity.ok(campLog);
    }

    @PutMapping
    public ResponseEntity<CampLogInfoResponse> updateCampLog(@RequestBody CampLogUpdateRequest request,
                                                             @ApiIgnore Principal principal){

        request.setRequesterEmail(principal.getName());
        CampLogInfoResponse updatedCampLog = campLogService.updateCampLog(request);

        return ResponseEntity.ok(updatedCampLog);
    }

    @DeleteMapping("/{campLogId}")
    public ResponseEntity<?> deleteCampLog(@PathVariable Long campLogId,
                                           @ApiIgnore Principal principal){

        campLogService.deleteCampLog(campLogId, principal.getName());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{campLogId}")
    public ResponseEntity<CampLogLikeResponse> checkOrUncheckLikeOnCampLog(@PathVariable Long campLogId,
                                                         @ApiIgnore Principal principal){

        CampLogLikeResponse campLogLikeResponse
                = campLogService.checkOrUncheckLikeOnCampLog(campLogId, principal.getName());

        return new ResponseEntity(campLogLikeResponse, HttpStatus.OK);
    }

}
