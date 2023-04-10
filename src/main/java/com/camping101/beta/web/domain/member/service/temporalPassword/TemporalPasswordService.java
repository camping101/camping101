package com.camping101.beta.web.domain.member.service.temporalPassword;

public interface TemporalPasswordService {

    void sendTemporalPassword(String email);

    boolean isTemporalPasswordMatching(String temporalPasswordToCompare, Long memberId);

}
