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
@SpringBootTest(classes = ChangePasswordRequestTest.class)
class ChangePasswordRequestTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    private ChangePasswordRequest getRequest(String email, String password, String rePassword) {
        return new ChangePasswordRequest(email, password, rePassword);
    }

    @Test
    void success() {
        Assertions.assertTrue(validator.validate(getRequest("test@user.com", "1irstPassw@rd", "1irstPassw@rd")).isEmpty());
    }

    @Test
    void fail() {
        Assertions.assertEquals(5, validator.validate(getRequest("", "1irstP", "1rst")).size());
        Assertions.assertEquals(4, validator.validate(getRequest("email", "1irstP", "1rst")).size());
        Assertions.assertEquals(3, validator.validate(getRequest("email@user.com", "1irstP", "1rst")).size());
        Assertions.assertEquals(2, validator.validate(getRequest("email@user.com", "F!rstP@ssw0rd", "1rst")).size());
        Assertions.assertEquals(0, validator.validate(getRequest("email@user.com", "F!rstP@ssw0rd", "F!rstP@ssw0rd")).size());
        validator.validate(getRequest("", "", "1"))
                .forEach(v -> {
                    System.out.println(v.getMessage());
                    Assertions.assertFalse(v.getMessage().startsWith("{"));
                });
    }
}