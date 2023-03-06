package com.camping101.beta.campLog.service;

import com.camping101.beta.campLog.repository.CampLogRepository;
import com.camping101.beta.regtag.entity.RecTag;
import com.camping101.beta.regtag.repository.RecTagRepository;
import com.camping101.beta.campLog.dto.*;
import com.camping101.beta.campLog.entity.CampLog;
import com.camping101.beta.member.repository.MemberRepository;
import com.camping101.beta.util.RedisClient;
import com.camping101.beta.util.S3FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CampLogService {

    private final MemberRepository memberRepository;
    private final CampLogRepository campLogRepository;
    private final RecTagRepository recTagRepository;
    private final S3FileUploader s3FileUploader;
    private final RedisClient redisClient;
    private final Logger logger = Logger.getLogger(CampLogService.class.getName());

    @Transactional
    public CampLogInfoResponse createCampLog(CampLogCreateRequest request){

        var member = memberRepository.findByEmail(request.getWriterEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        // TODO
//        var site = siteRepository.findById(request.getSiteId())
//                .orElseThrow(() -> new RuntimeException(""));

        if (request.getRecTags().size() > 4) {
            throw new RuntimeException("추천 태그는 4개까지만 등록 가능합니다.");
        }

        var newCampLog = campLogRepository.save(CampLog.from(request));

        try {
            List<String> imagePaths = saveImagesToS3AndGetPaths(request);
            newCampLog.setImagePaths(imagePaths);
        } catch (IOException e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.info(x.toString()));
        }

        newCampLog.setMember(member);
        newCampLog.setRecTags(parseRecTagsToOneString(getRecTagsByName(request.getRecTags())));
        // newCampLog.setSite(site);

        return CampLogInfoResponse.fromEntity(newCampLog);

    }

    private List<String> saveImagesToS3AndGetPaths(CampLogCreateRequest request) throws IOException {

        List<String> imagePaths = new ArrayList<>();

        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage1()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage2()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage3()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage4()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage5()));

        return imagePaths;
    }

    private List<RecTag> getRecTagsByName(List<String> recTagNames) {

        List<RecTag> recTags = new ArrayList<>();

        for (String recTagName : recTagNames) {
            var optionalRecTag = recTagRepository.findByName(recTagName);
            if (optionalRecTag.isEmpty()) {
                throw new RuntimeException("해당 추천 태그가 존재하지 않습니다.");
            }
            recTags.add(optionalRecTag.get());
        }

        return recTags;
    }

    private String parseRecTagsToOneString(List<RecTag> recTags){

        StringBuilder sb = new StringBuilder();

        for (RecTag recTagName : recTags) {
            sb.append(recTagName).append(",");
        }

        return sb.toString();
    }

    public CampLogListResponse getCampLogList(CampLogListRequest request){

        var page = PageRequest.of(request.getPageNumber(), request.getRecordSize(),
                Sort.Direction.DESC, "created_at");

        var campLogs = campLogRepository.findAll(page);

        return CampLogListResponse.fromEntity(campLogs);
    }

    @Transactional
    public CampLogInfoResponse getCampLogInfo(Long campLogId){

        var campLog = campLogRepository.findById(campLogId)
                .orElseThrow(() -> new RuntimeException(""));

        campLog.increaseViewCount();

        return CampLogInfoResponse.fromEntity(campLog);

    }

    @Transactional
    public CampLogInfoResponse updateCampLog(CampLogUpdateRequest request){

        var campLog = campLogRepository.findById(request.getCampLogId())
                .orElseThrow(() -> new RuntimeException(""));

        if (!campLog.getMember().getEmail().equals(request.getRequesterEmail())) {
            throw new RuntimeException("캠프 로그를 쓴 작성자만 수정이 가능합니다.");
        }

        if (request.getRecTags().size() > 4) {
            throw new RuntimeException("추천 태그는 4개까지만 등록 가능합니다.");
        }

        campLog.updateCampLog(request);

        try {
            List<String> imagePaths = saveImagesToS3AndGetPaths(request);
            campLog.setImagePaths(imagePaths);
        } catch (IOException e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.info(x.toString()));
        }

        campLog.setRecTags(parseRecTagsToOneString(getRecTagsByName(request.getRecTags())));

        return CampLogInfoResponse.fromEntity(campLog);
    }

    private List<String> saveImagesToS3AndGetPaths(CampLogUpdateRequest request) throws IOException {

        List<String> imagePaths = new ArrayList<>();

        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage1()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage2()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage3()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage4()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage5()));

        return imagePaths;
    }

    @Transactional
    public void deleteCampLog(Long campLogId, String requesterEmail){

        var campLog = campLogRepository.findById(campLogId)
                .orElseThrow(() -> new RuntimeException(""));

        if (!campLog.getMember().getEmail().equals(requesterEmail)) {
            throw new RuntimeException("캠프 로그를 쓴 작성자만 삭제가 가능합니다.");
        }

        campLogRepository.delete(campLog);

    }

    @Transactional
    public void checkOrUncheckLikeOnCampLog(Long campLogId, String requesterEmail){

        var campLog = campLogRepository.findById(campLogId)
                .orElseThrow(() -> new RuntimeException(""));

        var campLogLikeKey = CampLogLikeKey.getInstance(campLogId, requesterEmail).toString();
        var optionalLikeValue = redisClient.get(campLogLikeKey, Boolean.class);

        if (optionalLikeValue.isEmpty()) {
            redisClient.put(campLogLikeKey, true);
            campLog.increaseLikesCount();
        } else {
            redisClient.put(campLogLikeKey, false);
            campLog.decreaseLikesCount();
        }

    }

}
