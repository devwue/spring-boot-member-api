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
@SpringBootTest(classes = SignUpRequestTest.class)
class SignUpRequestTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    private SignUpRequest getRequest(String email,
                                     String name,
                                     String nick,
                                     String phoneAgency,
                                     String phoneNumber,
                                     String password,
                                     String rePassword) {
        return SignUpRequest.builder()
                .email(email)
                .name(name)
                .nickName(nick)
                .phoneAgency(phoneAgency)
                .phoneNumber(phoneNumber)
                .password(password)
                .passwordValidate(rePassword)
                .build();
    }

    @Test
    void success() {
        Assertions.assertTrue(validator.validate(getRequest("test@user.com",
                        "이산", "왕의 아들",
                        "SKT", "011-123-4567",
                        "Min1@mu8", "Min1@mu8"))
                .isEmpty());
    }

    @Test
    void fail() {
        Assertions.assertEquals(11, validator.validate(getRequest("",
                        "", "",
                        "", "011-123-45678",
                        "", "Min1@mu8")).size());
        Assertions.assertEquals(10, validator.validate(getRequest("san.lee@ korea.com",
                "", "",
                "", "011-123-45678",
                "", "Min1@mu8")).size());
        Assertions.assertEquals(9, validator.validate(getRequest("san.lee@korea.com",
                "", "",
                "", "011-123-45678",
                "", "Min1@mu8")).size());
        Assertions.assertEquals(8, validator.validate(getRequest("san.lee@korea.com",
                "이", "",
                "", "011-123-45678",
                "", "Min1@mu8")).size());
        Assertions.assertEquals(7, validator.validate(getRequest("san.lee@korea.com",
                "이산", "",
                "", "011-123-45678",
                "", "Min1@mu8")).size());
        Assertions.assertEquals(6, validator.validate(getRequest("san.lee@korea.com",
                "이산", "아",
                "", "011-123-45678",
                "", "Min1@mu8")).size());
        Assertions.assertEquals(5, validator.validate(getRequest("san.lee@korea.com",
                "이산", "왕의 아들",
                "", "011-123-45678",
                "", "Min1@mu8")).size());
        Assertions.assertEquals(4, validator.validate(getRequest("san.lee@korea.com",
                "이산", "왕의 아들",
                "SK", "011-123-45678",
                "", "Min1@mu8")).size());
        Assertions.assertEquals(3, validator.validate(getRequest("san.lee@korea.com",
                "이산", "왕의 아들",
                "SKT", "011-123-45678",
                "", "Min1@mu8")).size());
        Assertions.assertEquals(2, validator.validate(getRequest("san.lee@korea.com",
                "이산", "왕의 아들",
                "SKT", "011-123-45678",
                "Min1@m8!", "Min1@mu8")).size());
        Assertions.assertEquals(1, validator.validate(getRequest("san.lee@korea.com",
                "이산", "왕의 아들",
                "SKT", "011-123-45678",
                "Min1@m8!", "Min1@m8!")).size());
        Assertions.assertEquals(0, validator.validate(getRequest("san.lee@korea.com",
                "이산", "왕의 아들",
                "SKT", "011-1234-5678",
                "Min1@m8!", "Min1@m8!")).size());
        validator.validate(getRequest("t",
                        "이", "n",
                        "", "",
                        "Min1@m", "Min1@m8"))
                .forEach(v -> {
                    Assertions.assertFalse(v.getMessage().startsWith("{"));
                });
    }
}