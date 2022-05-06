package com.devwue.member.support;

import java.util.Random;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String maskingStr(String text, int length) {
        if (text == null || text.length() < length) {
            return text;
        }
        StringBuilder builder = new StringBuilder(text.length());
        builder.append("*".repeat(Math.max(0, length)));
        builder.append(text.substring(length));
        return builder.toString();
    }

    public static String maskingPhoneNumber(String phoneNumber) {
        Matcher matcher;
        matcher = Pattern.compile("^(01(?:0|1|[6-9]))[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$").matcher(phoneNumber);
        if (matcher.find()) {
            StringJoiner joiner = new StringJoiner("-");
            joiner.add(matcher.group(1));
            if (matcher.groupCount() > 2) {
                joiner.add(maskingStr(matcher.group(2), matcher.group(2).length()));
                joiner.add(matcher.group(3));
            } else {
                joiner.add(maskingStr(matcher.group(2), matcher.group(2).length()));
            }
            return joiner.toString();
        }
        return phoneNumber;
    }
}
