package com.devwue.member.controller.endpoint;

import com.devwue.member.support.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"local"})
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void verify_before_login() throws Exception {
        String payload = "{}";
        mockMvc.perform(post("/member/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void verify_withToken_no_user() throws Exception {
        String token = createAuthToken("no-signed-user");
        String payload = "{}";
        mockMvc.perform(post("/member/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @Sql("/member-create.sql")
    void verify_withToken_exist_user_and_validate() throws Exception {
        String email = "kildong.hong@devwue.com";
        String token = createAuthToken(email);
        String payload = "{}";

        mockMvc.perform(post("/member/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @Sql({"/member-create.sql","/phone-authentication-create.sql"})
    void verify_withToken_exist_user_success() throws Exception {
        String email = "kildong.hong@devwue.com";
        String token = createAuthToken(email);
        String payload = "{\n" +
                "  \"email\": \""+email+"\",\n" +
                "  \"phoneNumber\": \"010-1234-5678\",\n" +
                "  \"phoneToken\": \"123456\"\n" +
                "}";

        mockMvc.perform(post("/member/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @Sql("/member-create.sql")
    void memberPage() throws Exception {
        String token = createAuthToken("kildong.hong@devwue.com");

        mockMvc.perform(get("/member/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    private String createAuthToken(String email) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, null);
        return JwtUtil.newToken(authenticationToken);
    }
}