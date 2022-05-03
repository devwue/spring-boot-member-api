package com.devwue.member.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "가입자 검색 응답 모델")
@Getter
@Builder
public class MemberSearchDto {
    @Schema(description = "email", example = "test@user.com")
    private String email;
    @Schema(description = "이름", example = "허준")
    private String name;
    @Schema(description = "닉네임", example = "명의")
    private String nickName;
    @Schema(description = "핸드폰 번호", example = "01012345678")
    private String phoneNumber;
}
