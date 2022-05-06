package com.devwue.member.service;

import com.devwue.member.support.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PrivacyService {
    private final String securityKey;
    private final String iv;

    public PrivacyService(@Value("${app.crypto.securityKey}") String securityKey,
                          @Value("${app.crypto.iv}") String iv) {
        this.securityKey = securityKey;
        this.iv = iv;
    }

    public String encrypt(String text) {
        return Crypto.encrypt(text, securityKey, iv);
    }

    public String decrypt(String text) {
        return Crypto.decrypt(text, securityKey, iv);
    }
}
