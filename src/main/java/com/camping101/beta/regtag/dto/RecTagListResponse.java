package com.camping101.beta.regtag.dto;

import com.camping101.beta.admin.recTag.dto.AdminRecTagInfoResponse;
import com.camping101.beta.regtag.entity.RecTag;
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
public class RecTagListResponse {

    long total;
    int pageNumber = 1;
    int recordSize = 10;
    List<AdminRecTagInfoResponse> recTags = new ArrayList<>();

    public static RecTagListResponse fromEntity(Page<RecTag> response) {
        return RecTagListResponse.builder()
                .total(response.getTotalElements())
                .pageNumber(response.getNumber())
                .recordSize(response.getNumberOfElements())
                .recTags(response.getContent().stream()
                        .map(AdminRecTagInfoResponse::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
