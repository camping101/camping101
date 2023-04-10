package com.camping101.beta.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class RandomCode {

    public static String createRandomUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createRandomEightString(){
        return RandomStringUtils.randomAlphanumeric(8);
    }

}
