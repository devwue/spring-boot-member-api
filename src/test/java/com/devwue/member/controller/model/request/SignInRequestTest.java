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
@SpringBootTest(classes = SignInRequestTest.class)
class SignInRequestTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    private SignInRequest getRequest(String email, String password) {
        return new SignInRequest(email, password);
    }

    @Test
    void success() {
        Assertions.assertTrue(validator.validate(getRequest("test@user.com", "Min1@mu8")).isEmpty());
    }

    @Test
    void fail() {
        validator.validate(getRequest("", "Min1@mu8")).forEach(v -> System.out.println(v.toString()));
        Assertions.assertEquals(2, validator.validate(getRequest("", "Min1@mu8")).size());
        Assertions.assertEquals(1, validator.validate(getRequest("test@user", "Mim1um8!")).size());
        Assertions.assertEquals(1, validator.validate(getRequest("test@user..com", "Mim1um8!")).size());
        Assertions.assertEquals(1, validator.validate(getRequest("test.@user.com", "Mim1um8!")).size());
        Assertions.assertEquals(1, validator.validate(getRequest("test.user@user.com", "Mim1um8")).size());
        Assertions.assertEquals(1, validator.validate(getRequest("test.user@user.com", "Mim!um*")).size());
        validator.validate(getRequest("test.user@user.com", "Mim!um*"))
                .forEach(v -> {
                    System.out.println(v.getMessage());
                    Assertions.assertFalse(v.getMessage().startsWith("{"));
                });
    }
}