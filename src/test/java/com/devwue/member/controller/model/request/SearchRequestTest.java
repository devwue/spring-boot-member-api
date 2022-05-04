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
@SpringBootTest(classes = SearchRequestTest.class)
class SearchRequestTest {
    @Autowired
    private LocalValidatorFactoryBean validator;

    private SearchRequest getRequest(String type, String keyword) {
        return new SearchRequest(type, keyword);
    }

    @Test
    void success() {
        SearchRequest request = getRequest("email", "test@user.com");
        Assertions.assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void fail() {
        Assertions.assertEquals(1, validator.validate( getRequest("", "test@user.com")).size());
        Assertions.assertEquals(2, validator.validate( getRequest("nick", "")).size());
        Assertions.assertEquals(2, validator.validate( getRequest("phone", "")).size());
        Assertions.assertEquals(2, validator.validate( getRequest("email", "")).size());
        Assertions.assertEquals(3, validator.validate( getRequest("name", "")).size());
        Assertions.assertEquals(1, validator.validate( getRequest("name", "이산")).size());
        validator.validate(getRequest("emai", "test@usercom")).forEach(v -> {
            Assertions.assertFalse(v.getMessage().startsWith("{"));
        });
    }
}