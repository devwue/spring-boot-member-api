package com.devwue.member.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Schema(description = "비밀번호 변경 요청 모델")
@Getter
@AllArgsConstructor
public class ChangePasswordRequest {
    @Schema(description = "email", example = "test@user.com")
    @NotBlank(message = "{email.notblank}")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "{email.pattern}")
    private String email;

    @Schema(description = "변경할 비밀번호", example = "password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,18}$", message = "{password.pattern}")
    private String password;

    @Schema(description = "변경할 비밀번호", example = "password")
    @NotBlank(message = "{password.validate}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,18}$", message = "{password.pattern}")
    private String passwordValidate;

    @AssertTrue(message = "{password.validate}")
    private boolean isValid() {
        return password != null && password.equals(passwordValidate);
    }
}
