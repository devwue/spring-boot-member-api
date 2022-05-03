package com.devwue.member.controller.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles({"local"})
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void sendToken_validate() throws Exception {
        String payload = "{\n" +
                "  \"feature\" : \"SIGN_UP\",\n" +
                "  \"phoneNumber\": \"0101234567 \"\n" +
                "}";
        mockMvc.perform(post("/phone/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("FAIL"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void sendToken_success() throws Exception {
        String payload = "{\n" +
                "  \"feature\" : \"SIGN_UP\",\n" +
                "  \"phoneNumber\": \"01012345678\"\n" +
                "}";
        mockMvc.perform(post("/phone/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @Sql("/phone-authentication-create.sql")
    void verify_withToken_validate() throws Exception {
        String payload = "{\n" +
                "  \"feature\": \"SIGN_UP\",\n" +
                "  \"phoneNumber\": \"010-1234-5678\",\n" +
                "  \"phoneToken\": 123457\n" +
                "}";

        mockMvc.perform(post("/phone/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Sql("/phone-authentication-create.sql")
    void verify_withToken_success() throws Exception {
        String payload = "{\n" +
                "  \"feature\": \"SIGN_UP\",\n" +
                "  \"phoneNumber\": \"010-1234-5678\",\n" +
                "  \"phoneToken\": 123456\n" +
                "}";

        mockMvc.perform(post("/phone/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }


    @Test
    void signUp_validation() throws Exception {
        String payload = "{}";
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    @Sql("/phone-authentication-create.sql")
    void signUp_success() throws Exception {
        String payload = "{\n" +
                "  \"email\" : \"kildong.hong@devwue.com\",\n" +
                "  \"name\" : \"홍길동\",\n" +
                "  \"nickName\" : \"의적\",\n" +
                "  \"phoneAgency\": \"SKT\",\n" +
                "  \"phoneNumber\": \"010-1234-5678\",\n" +
                "  \"password\": \"FirstPassword!2345\",\n" +
                "  \"passwordValidate\": \"FirstPassword!2345\"\n" +
                "}";
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    @Sql({"/phone-authentication-create.sql", "/member-create.sql"})
    void signUp_duplicated() throws Exception {
        String payload = "{\n" +
                "  \"email\" : \"kildong.hong@devwue.com\",\n" +
                "  \"name\" : \"홍길동\",\n" +
                "  \"nickName\" : \"의적\",\n" +
                "  \"phoneAgency\": \"SKT\",\n" +
                "  \"phoneNumber\": \"010-1234-5678\",\n" +
                "  \"password\": \"FirstPassword!2345\",\n" +
                "  \"passwordValidate\": \"FirstPassword!2345\"\n" +
                "}";
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    void login_validate() throws Exception {
        String payload = "{\n" +
                "  \"email\" : \"kildong.hong@\",\n" +
                "  \"password\": \"illegal password\"\n" +
                "}";
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    void login_unAuthorized() throws Exception {
        String payload = "{\n" +
                "  \"email\" : \"kildong.hong@devwue.com\",\n" +
                "  \"password\": \"FirstPassword!2345\"\n" +
                "}";
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    @Sql("/member-create.sql")
    void login_success() throws Exception {
        String payload = "{\n" +
                "  \"email\" : \"kildong.hong@devwue.com\",\n" +
                "  \"password\": \"FirstPassword!2345\"\n" +
                "}";
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.email").exists())
                .andExpect(jsonPath("$.data.nickName").exists())
                .andExpect(jsonPath("$.data.phoneValidate").exists())
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    void search_validate() throws Exception {
        String payload = "{\n" +
                "  \"identifierType\" : \"email\",\n" +
                "  \"keyword\": \"g\"\n" +
                "}";
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("FAIL"));
    }

    @Test
    void search_result_empty() throws Exception {
        String payload = "{\n" +
                "  \"identifierType\" : \"email\",\n" +
                "  \"keyword\": \"gildong.hong@devwue.com\"\n" +
                "}";
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @Sql("/member-create.sql")
    void search_result_found() throws Exception {
        String payload = "{\n" +
                "  \"identifierType\" : \"email\",\n" +
                "  \"keyword\": \"kildong.hong@devwue.com\"\n" +
                "}";
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isMap());
    }

    @Test
    @Sql("/phone-authentication-password.sql")
    void verify_withToken_for_changePassword_valid() throws Exception {
        String payload = "{\n" +
                "  \"feature\": \"RESET_PASSWORD\",\n" +
                "  \"phoneNumber\": \"010-1234-5678\",\n" +
                "  \"phoneToken\": 123456\n" +
                "}";

        mockMvc.perform(post("/phone/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Sql("/phone-authentication-password.sql")
    void verify_withToken_for_changePassword_success() throws Exception {
        String payload = "{\n" +
                "  \"feature\": \"RESET_PASSWORD\",\n" +
                "  \"phoneNumber\": \"010-1234-5678\",\n" +
                "  \"phoneToken\": 223456\n" +
                "}";

        mockMvc.perform(post("/phone/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Sql({"/phone-authentication-create.sql", "/member-create.sql"})
    void changePassword_validate_password() throws Exception {
        String email = "kildong.hong@devwue.com";
        String payload = "{\n" +
                "  \"email\": \""+email+"\",\n" +
                "  \"password\": \"Second123password!\",\n" +
                "  \"passwordValidate\": \"Second123passwor!\"\n" +
                "}";
        mockMvc.perform(put("/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("FAIL"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @Sql({"/phone-authentication-create.sql", "/member-create.sql"})
    void changePassword_verify_token() throws Exception {
        String email = "kildong.hong@devwue.com";
        String payload = "{\n" +
                "  \"email\": \""+email+"\",\n" +
                "  \"password\": \"Second123password!\",\n" +
                "  \"passwordValidate\": \"Second123password!\"\n" +
                "}";
        mockMvc.perform(put("/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("FAIL"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Sql({"/phone-authentication-password.sql", "/member-create.sql"})
    void changePassword_success() throws Exception {
        String email = "kildong.hong@devwue.com";
        String payload = "{\n" +
                "  \"email\": \""+email+"\",\n" +
                "  \"password\": \"Second123password!\",\n" +
                "  \"passwordValidate\": \"Second123password!\"\n" +
                "}";
        mockMvc.perform(put("/change/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isMap());
    }
}