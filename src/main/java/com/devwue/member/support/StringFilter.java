package com.devwue.member.support;

import java.util.Random;

public class StringFilter {
    private StringFilter() {}
    public static String onlyNumber(String given) {
        if (given == null) {
            return "";
        }
        return given.replaceAll("\\D+", "");
    }

    public static String createNumberToken(Integer size) {
        Random random = new Random();
        return random.ints(size, 48, 48+10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
