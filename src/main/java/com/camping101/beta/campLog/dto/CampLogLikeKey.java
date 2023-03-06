package com.camping101.beta.campLog.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
public class CampLogLikeKey implements Serializable {

    private long campLogId;
    private String memberEmail;

    private CampLogLikeKey(){}

    private CampLogLikeKey(long campLogId, String memberEmail) {
        this.campLogId = campLogId;
        this.memberEmail = memberEmail;
    }

    private static CampLogLikeKey campLogLikeKey;

    public static CampLogLikeKey getInstance(long campLogId, String memberEmail) {
        if (Objects.isNull(campLogLikeKey)) {
            return new CampLogLikeKey(campLogId, memberEmail);
        }
        campLogLikeKey.setCampLogId(campLogId);
        campLogLikeKey.setMemberEmail(memberEmail);
        return campLogLikeKey;
    }

    public String toString() {
        return String.format("%d:%s",campLogId,memberEmail);
    }
}
