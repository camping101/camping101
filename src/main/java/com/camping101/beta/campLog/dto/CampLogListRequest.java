package com.camping101.beta.campLog.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampLogListRequest {

    int pageNumber = 1;
    int recordSize = 10;

}
