package com.camping101.beta.admin.campAuth.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CampAuthIdListRequest {

    List<Long> campAuthId = new ArrayList<>();

}
