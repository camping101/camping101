package com.camping101.beta.site.entity;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteYn {

    private String openYn;
    private String animalYn;
    private String campnicYn;
    private String stayOverYn;
    private String trailerYn;
    private String campCarYn;

}
