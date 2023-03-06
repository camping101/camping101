package com.camping101.beta.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class RandomCode {

    public static String createRandomUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createRandomEightString(){
        // 랜덤 아스키 8자리
        return RandomStringUtils.randomAscii(8);
    }

}
