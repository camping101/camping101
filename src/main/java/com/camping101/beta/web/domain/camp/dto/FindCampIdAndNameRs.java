
package com.camping101.beta.web.domain.camp.dto;

import com.camping101.beta.db.entity.camp.Camp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindCampIdAndNameRs {

    private Long campId;
    private String campName;

    public static FindCampIdAndNameRs createFindCampIdAndNameRs(Camp camp) {

        return FindCampIdAndNameRs
            .builder()
            .campId(camp.getCampId())
            .campName(camp.getName())
            .build();

    }

}
