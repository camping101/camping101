package com.camping101.beta.db.entity.campauth;

import static com.camping101.beta.db.entity.campauth.CampAuthStatus.AUTHORIZED;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CampAuthTest {

    @Test
    @DisplayName("관리자는 캠핑장 승인 요청을 수락할 수 있다.")
    void editCampAuthStatusTest(){
        // given
        CampAuth campAuth = new CampAuth();
        // when
        campAuth.editCampAuthStatus();
        // then
        Assertions.assertThat(campAuth.getCampAuthStatus()).isEqualTo(AUTHORIZED);
    }

}