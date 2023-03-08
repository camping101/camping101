package com.camping101.beta.regtag.dto;

import com.camping101.beta.regtag.entity.RecTag;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecTagListResponse {

    long total;
    int pageNumber = 0;
    int recordSize = 10;
    List<RecTag> recTags = new ArrayList<>();

    public static RecTagListResponse fromEntity(Page<RecTag> response) {
        return RecTagListResponse.builder()
                .total(response.getTotalElements())
                .pageNumber(response.getNumber())
                .recordSize(response.getNumberOfElements())
                .recTags(response.getContent())
                .build();
    }
}
