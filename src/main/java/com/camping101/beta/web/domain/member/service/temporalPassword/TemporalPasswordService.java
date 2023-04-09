package com.camping101.beta.web.domain.member.service.temporalPassword;

public interface TemporalPasswordService {

    void sendTemporalPassword(Long memberId);

    boolean isTemporalPasswordMatching(Long memberId, String temporalPasswordToCompare);

}
