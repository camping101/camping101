package com.camping101.beta.web.domain.campLog.dto;

import com.camping101.beta.db.entity.campLog.CampLog;
import lombok.*;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampLogListResponse {

    long total;
    int pageNumber = 0;
    int recordSize = 10;
    List<CampLogInfoResponse> campLogs = new ArrayList<>();

    public static CampLogListResponse fromEntity(Page<CampLog> campLogs) {
        return CampLogListResponse.builder()
                .total(campLogs.getTotalElements())
                .pageNumber(campLogs.getNumber())
                .recordSize(campLogs.getNumberOfElements())
                .campLogs(campLogs.getContent().stream()
                        .map(CampLogInfoResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
