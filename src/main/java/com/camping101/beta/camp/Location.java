package com.camping101.beta.camp;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    private String environment;
    private String addr1;
    private String addr2;
    private String latitude;
    private String longitude;

}
