package com.camping101.beta.db.entity.camp;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FacilityCnt {

    private int toiletCnt;
    private int showerCnt;
    private int waterProofCnt;

}
