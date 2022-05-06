package com.devwue.member.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringFilterTest {

    @Test
    void onlyNumber() {
        Assertions.assertEquals("01012345678", StringFilter.onlyNumber("010-1234-5678"));
        Assertions.assertEquals("01", StringFilter.onlyNumber("String01Inert!@$"));
    }

    @Test
    void createNumberToken() {
        Assertions.assertEquals(false, StringFilter.createNumberToken(6).matches("\\D+"));
        Assertions.assertEquals(6, StringFilter.createNumberToken(6).length());
    }

    @Test
    void maskingStr() {
        Assertions.assertEquals("***4", StringFilter.maskingStr("1234", 3));
        Assertions.assertEquals("****", StringFilter.maskingStr("1234", 4));
    }

    @Test
    void maskingPhoneNumber() {
        Assertions.assertEquals("011-***-5678",StringFilter.maskingPhoneNumber("0112345678"));
        Assertions.assertEquals("010-***-5678",StringFilter.maskingPhoneNumber("0102345678"));
        Assertions.assertEquals("019-****-5678",StringFilter.maskingPhoneNumber("01912345678"));
    }
}