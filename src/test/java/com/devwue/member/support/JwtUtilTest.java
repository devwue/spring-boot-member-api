package com.devwue.member.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


class JwtUtilTest {

    @Test
    void newToken() {
        String user = "kildong.hong@devwue.com";
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
        String token = JwtUtil.newToken(authenticationToken);

        System.out.println(token);
        Assertions.assertNotNull(token);

        String decode = JwtUtil.parseToken(token);
        Assertions.assertEquals(user, decode);

        Boolean parse = JwtUtil.validateToken(token);
        Assertions.assertTrue(parse);
    }
}