package com.camping101.beta.camp.service;

import com.camping101.beta.camp.dto.CampCreateRequest;
import com.camping101.beta.camp.dto.CampCreateResponse;
import com.camping101.beta.camp.dto.CampListResponse;
import com.camping101.beta.camp.dto.CampModifyRequest;
import com.camping101.beta.camp.dto.CampModifyResponse;
import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.repository.CampRepository;
import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CampService {

    private final CampRepository campRepository;
    private final MemberRepository memberRepository;

    // 캠핑장 서비스 이용 요청
    public CampCreateResponse registerCamp(CampCreateRequest campCreateRequest) {

        Camp camp = Camp.toEntity(campCreateRequest);
        campRepository.save(camp);

        Long memberId = campCreateRequest.getMemberId();
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 회원이 없습니다");
        });

        camp.addMember(findMember); // 변경 감지

        return Camp.toCampCreateResponse(camp);
    }

    // 캠핑장 목록 조회(페이징 처리 하기) (주인)
    @Transactional(readOnly = true)
    public List<CampListResponse> findOwnerCampList(Pageable pageable, Long memberId) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하는 회원이 없습니다.");
        });

        Page<Camp> camps = campRepository.findAllByMember(pageable, findMember);
        return camps.stream().map(Camp::toCampListResponse).collect(Collectors.toList());
    }

    // 캠핑장 목록 조회(페이징 처리 하기) (손님 + 비회원)
    @Transactional(readOnly = true)
    public List<CampListResponse> findCampList(Pageable pageable) {

        Page<Camp> camps = campRepository.findAll(pageable);

        return camps.stream().map(Camp::toCampListResponse).collect(Collectors.toList());
    }

    // 캠핑장 상세 정보 조회
    @Transactional(readOnly = true)
    public Camp findCampDetails(Long campId) {
        Camp camp = campRepository.findById(campId).orElseThrow(() -> {
            throw new RuntimeException("회원 아이디가 존재하지 않습니다");
        });
    }

    // 캠핑장 상세 정보 수정
    public CampModifyResponse modifyCamp(CampModifyRequest campEditRequest) {

        Camp camp = campRepository.findById(campEditRequest.getCampId()).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 캠핑장입니다.");
        });

        Camp modifiedCamp = camp.updateCamp(campEditRequest);

        return Camp.toCampModifyResponse(modifiedCamp);
    }

    // 캠핑장 서비스 탈퇴 요청
    public void removeCamp(Long campId) {
        campRepository.deleteById(campId);
    }







}
