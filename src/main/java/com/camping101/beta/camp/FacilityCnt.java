package com.camping101.beta.camp;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityCnt {

    private int toiletCnt;
    private int showerCnt;
    private int waterProofCnt;

}
