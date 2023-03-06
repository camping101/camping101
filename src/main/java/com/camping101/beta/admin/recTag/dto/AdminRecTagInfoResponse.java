package com.camping101.beta.admin.recTag.dto;

import com.camping101.beta.regtag.entity.RecTag;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRecTagInfoResponse {

    private Long recTagId;
    private String name;
    private boolean useYn;

    public static AdminRecTagInfoResponse fromEntity(RecTag recTag) {
        return AdminRecTagInfoResponse.builder()
                .recTagId(recTag.getRecTagId())
                .name(recTag.getName())
                .useYn(recTag.isUseYn())
                .build();
    }
}
