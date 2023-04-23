package com.camping101.beta.web.domain.siteseat.dto;

import com.camping101.beta.db.entity.siteseat.SiteSeat;
import lombok.Getter;

@Getter
public class SiteSeatDto {

    private String seatNumber;
    private boolean isReserved;

    public SiteSeatDto(SiteSeat siteSeat) {
        this.seatNumber = siteSeat.getSeatNumber();
        this.isReserved = siteSeat.isReserved();
    }
}
