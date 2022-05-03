package com.devwue.member.controller.model.request;

import com.devwue.member.annotation.Enum;
import com.devwue.member.model.enums.MobileAgency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.*;

@Schema(description = "회원 가입 요청 모델")
@Getter
public class SignUpRequest {
    @Schema(description = "로그인시 사용하는 email 주소", example = "test@user.com")
    @NotBlank(message = "email은 필수 입니다.")
    @Email(message = "email 주소만 가능 합니다.")
    private String email;

    @Schema(description = "이름", example = "허준")
    @NotBlank(message = "이름은 필수 입니다.")
    @Size(min=2, max = 8, message = "이름은 2 ~ 8자까지 가능 합니다.")
    private String name;

    @Schema(description = "닉네임", example = "명의")
    @NotBlank(message = "닉네임은 필수 입니다.")
    @Size(min=2, max = 8, message = "닉네임은 2 ~ 8자까지 가능 합니다.")
    private String nickName;

    @Schema(description = "핸드폰 통신사", example = "SKT", allowableValues = {"SKT", "KT", "LGT", "SKM", "KTM", "LGM"})
    @NotBlank(message = "통신사를 선택 하세요.")
    @Enum(enumClass = MobileAgency.class, message = "SKT, KT, LGT, SKM, KTM, LGM 만 선택 가능 합니다.")
    private String phoneAgency;

    @Schema(description = "핸드폰 번호", example = "010-1234-5678")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "번호는 10~11자리 숫자만 가능 합니다.")
    private String phoneNumber;

    @Schema(description = "로그인 비밀번호", example = "Hello7W@&rd")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,18}$", message = "비밀번호는 영어와 숫자를 포함한 6~18자리 까지 가능 합니다.")
    private String password;

    @Schema(description = "로그인 비밀번호", example = "Hello7W@&rd")
    @NotBlank(message = "비밀번호를 동일하게 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,18}$", message = "비밀번호는 영어와 숫자를 포함한 6~18자리 까지 가능 합니다.")
    private String passwordValidate;

    @AssertTrue(message = "비밀번호를 동일하게 입력해 주세요.")
    private boolean isValid() {
        return password != null && password.equals(passwordValidate);
    }
}
