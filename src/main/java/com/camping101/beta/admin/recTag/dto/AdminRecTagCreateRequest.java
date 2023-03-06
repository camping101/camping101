package com.camping101.beta.admin.recTag.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRecTagCreateRequest {

    private Long campLogId;

    private String name;
    private boolean useYn;

}
