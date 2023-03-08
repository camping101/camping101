package com.camping101.beta.admin.campAuth.service;

import static com.camping101.beta.admin.campAuth.exception.ErrorCode.CAMP_AUTH_NOT_FOUND;

import com.camping101.beta.admin.campAuth.dto.CampAuthAddResponse;
import com.camping101.beta.admin.campAuth.dto.CampAuthListResponse;
import com.camping101.beta.admin.campAuth.entity.CampAuth;
import com.camping101.beta.admin.campAuth.exception.CampAuthException;
import com.camping101.beta.admin.campAuth.repository.CampAuthRepository;
import com.camping101.beta.camp.entity.Camp;
import com.camping101.beta.camp.repository.CampRepository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
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
    private final EntityManager entityManager;

    //      1. 회원(주인)은 회원가입 후에 캠핑장 관리 서비스 이용 요청을 할 수 있다.
//      2. 관리자가 승인을 하면 캠핑장 관리 서비스 이용 가능
    @Transactional(readOnly = true)
    public Page<CampAuthListResponse> findCampAuthList(Pageable pageable) {

        Page<CampAuth> campAuthList = campAuthRepository.findAll(pageable);

        return campAuthList.map(campAuth -> new CampAuthListResponse(
            campAuth.getCampAuthId(),
            campAuth.getCamp().getCampId(),
            campAuth.getCamp().getName(),
            String.valueOf(campAuth.getCampAuthStatus())));
    }

    // 캠핑장 상세에서 승인
    public CampAuthAddResponse permitCampAuth(Long campAuthId) {
//        이거 버리자
//        CampAuth findCampAuth = campAuthRepository.findById(campAuthId).orElseThrow(() -> {
//            throw new RuntimeException("A - 캠핑장이 존재하지 않습니다");
//        });
//
//        Camp findCamp = findCampAuth.getCamp();

        CampAuth findCampAuth = campAuthRepository.findByCampAuthId11(campAuthId).orElseThrow(() -> {
                throw new CampAuthException(CAMP_AUTH_NOT_FOUND);
            });

        Camp findCamp = findCampAuth.getCamp();

        findCamp.editManageStatus();
        findCampAuth.editCampAuthStatus();

        return CampAuth.toCampAuthAddResponse(findCampAuth, findCamp);
    }

    // 캠핑장 목록조회에서 단체 승인
    public List<CampAuthAddResponse> permitCampAuthList(
        List<Long> campAuthIds) {

        List<CampAuthAddResponse> campAuthAddResponses = new ArrayList<>();

        for (Long campAuthId : campAuthIds) {

            CampAuthAddResponse campAuthAddResponse = permitCampAuth(campAuthId);
            campAuthAddResponses.add(campAuthAddResponse);
        }

        return campAuthAddResponses;

    }
}
