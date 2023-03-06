package com.camping101.beta.admin.service;

import com.camping101.beta.admin.dto.CampAuthAddResponse;
import com.camping101.beta.admin.dto.CampAuthListResponse;
import com.camping101.beta.admin.entity.CampAuth;
import com.camping101.beta.admin.repository.CampAuthRepository;
import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.repository.CampRepository;
import java.util.ArrayList;
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
public class CampAuthService {

    private final CampAuthRepository campAuthRepository;
    private final CampRepository campRepository;

//      1. 회원(주인)은 회원가입 후에 캠핑장 관리 서비스 이용 요청을 할 수 있다.
//      2. 관리자가 승인을 하면 캠핑장 관리 서비스 이용 가능
    public List<CampAuthListResponse> findCampAuthList(Pageable pageable) {

        Page<CampAuth> campAuthList = campAuthRepository.findAll(pageable);

        return campAuthList.stream().map(campAuth -> new CampAuthListResponse(
            campAuth.getCampAuthId(),
            campAuth.getCamp().getCampId(),
            campAuth.getCamp().getName(),
            String.valueOf(campAuth.getCampAuthStatus()))).collect(Collectors.toList());
    }

    // 캠핑장 상세에서 승인
    public CampAuthAddResponse permitCampAuth(Long campAuthId) {

        CampAuth findCampAuth = campAuthRepository.findById(campAuthId).orElseThrow(() -> {
            throw new RuntimeException("A - 캠핑장이 존재하지 않습니다");
        });

        Camp findCamp = campRepository.findById(findCampAuth.getCamp().getCampId())
            .orElseThrow(() -> {
                throw new RuntimeException("존재하는 캠핑장이 없습니다");
            });

        findCamp.editManageStatus(findCamp);

        return CampAuth.toCampAuthAddResponse(findCampAuth, findCamp);
    }

    // 캠핑장 목록조회에서 단체 승인
    public List<CampAuthAddResponse> permitCampAuthList(List<Long> campAuthIds) {

        List<CampAuthAddResponse> campAuthAddResponses = new ArrayList<>();

        for (Long campAuthId : campAuthIds) {

            CampAuthAddResponse campAuthAddResponse = permitCampAuth(campAuthId);
            campAuthAddResponses.add(campAuthAddResponse);

        }

        return campAuthAddResponses;

    }
}
