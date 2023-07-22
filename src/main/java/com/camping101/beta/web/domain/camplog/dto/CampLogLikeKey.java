package com.camping101.beta.web.domain.camplog.dto;

import java.io.Serializable;

public class CampLogLikeKey implements Serializable {

    public static String getKey(Long campLogId, String memberEmail) {
        return String.format("%d:%s", campLogId, memberEmail);
    }

}
