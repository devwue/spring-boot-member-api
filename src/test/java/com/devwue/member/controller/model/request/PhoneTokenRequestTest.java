package com.devwue.member.controller.model.request;

import com.devwue.member.config.MessageSourceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@Configuration
@Import(MessageSourceConfig.class)
@SpringBootTest(classes = PhoneTokenRequestTest.class)
class PhoneTokenRequestTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    void success() {
        PhoneTokenRequest request = getRequest("SIGN_UP", "010-1234-5678");
        Assertions.assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void fail() {
        Assertions.assertEquals(1, validator.validate(getRequest("SIGN_U", "01012345678")).size());
        Assertions.assertEquals(2, validator.validate(getRequest("SIGN_U", "101234567")).size());
        validator.validate(getRequest("SIGN", "010123456"))
                .forEach(v -> {
                    System.out.println(v.getMessage());
                    Assertions.assertFalse(v.getMessage().startsWith("{"));
                });
    }

    private PhoneTokenRequest getRequest(String feature, String phoneNumber) {
        return new PhoneTokenRequest(feature, phoneNumber);
    }
}