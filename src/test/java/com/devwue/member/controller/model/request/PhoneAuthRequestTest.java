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
@SpringBootTest(classes = PhoneAuthRequestTest.class)
class PhoneAuthRequestTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    void success() {
        PhoneAuthRequest request = getRequest("SIGN_UP", "010-1234-5678", "000000");
        Assertions.assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void fail() {
        Assertions.assertEquals(1, validator.validate(getRequest("SIGN_U", "010-1234-5678", "123456")).size());
        Assertions.assertEquals(2, validator.validate(getRequest("SIGN_U", "010-1234-567", "123456")).size());
        Assertions.assertEquals(3, validator.validate(getRequest("SIGN_U", "010-1234-567", "12345")).size());
        validator.validate(getRequest("SIGN_U", "010-1234-567", "12345"))
                .forEach(v -> Assertions.assertFalse(v.getMessage().startsWith("{")));
    }

    private PhoneAuthRequest getRequest(String feature, String phoneNumber, String phoneToken) {
        return new PhoneAuthRequest(feature, phoneNumber, phoneToken);
    }
}