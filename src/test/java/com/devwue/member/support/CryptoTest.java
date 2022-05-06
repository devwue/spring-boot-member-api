package com.devwue.member.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CryptoTest {

    @Test
    void encrypt() {
        String securityKey = "security";
        String iv = "testVectorkey123";

        String plainText = "사람이름이몇자일까";
        String encrypted = Crypto.encrypt(plainText, securityKey, iv);
        System.out.println(encrypted);
        System.out.println(encrypted.length());
        Assertions.assertEquals(encrypted, Crypto.encrypt(plainText, securityKey, iv));

        Assertions.assertNotNull(encrypted);
        Assertions.assertEquals(plainText, Crypto.decrypt(encrypted, securityKey, iv));
    }

    @Test
    void encrypt_length() {
        String securityKey = "security";
        String iv = "testVectorkey123";

        String plainText = "한글로오십자가되면길이는어떻게될까궁금해서한번해보자하자가자말자더자구자양자소행성지구넷다여일여아열";
        String encrypted = Crypto.encrypt(plainText, securityKey, iv);

        System.out.println(encrypted);
        System.out.println(encrypted.length());

        Assertions.assertNotNull(encrypted);
        Assertions.assertEquals(plainText, Crypto.decrypt(encrypted, securityKey, iv));
    }
}