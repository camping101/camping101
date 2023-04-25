package com.camping101.beta.db.entity.siteseat;

import com.camping101.beta.db.entity.site.Site;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class SiteSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long siteSeatId;
    private String seatNumber;

    private Integer humanCapacity;

    private Boolean isReserved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    public static SiteSeat createSiteSeat(String seatNumber, Integer humanCapacity) {

        SiteSeat siteSeat = new SiteSeat();

        siteSeat.seatNumber = seatNumber;
        siteSeat.isReserved = Boolean.FALSE;
        siteSeat.humanCapacity = humanCapacity;

        return siteSeat;
    }

    public void changeSite(Site site) {
        this.site = site;

    }

}
