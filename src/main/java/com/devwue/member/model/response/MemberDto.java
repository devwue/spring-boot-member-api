package com.devwue.member.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "회원 정보 기본 응답 모델")
@Getter
@Builder
public class MemberDto {
    @Schema(description = "email", example = "test@user.com")
    private String email;
    @Schema(description = "이름", example = "김두환")
    private String name;
    @Schema(description = "닉네임", example = "장군의 아들")
    private String nickName;
    @Schema(description = "핸드폰 번호", example = "0111235678")
    private String phoneNumber;
    @Schema(description = "핸드폰 통신사", example = "SKT")
    private String phoneAgency;
    @Schema(description = "핸드폰 인증 여부", example = "false")
    private Boolean phoneValidate;
}
