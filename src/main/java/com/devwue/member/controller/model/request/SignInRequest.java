package com.devwue.member.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Schema(description = "로그인 요청 모델")
@Getter
@AllArgsConstructor
public class SignInRequest {
    @Schema(description = "email", example = "test@user.com")
    @NotBlank(message = "{email.notblank}")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "{email.pattern}")
    private String email;

    @Schema(description = "비밀번호", example = "password")
    @NotBlank(message = "{password.notblank}")
    @Size(min=8, max = 18, message = "{password.size}")
    private String password;
}
