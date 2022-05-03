package com.devwue.member.controller.model.request;

import com.devwue.member.annotation.Enum;
import com.devwue.member.model.enums.IdentifierType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "계정 검색 요청 모델")
@Getter
public class SearchRequest {
    @Schema(description = "계정 검색 기준", example = "email", allowableValues = {"email", "nick", "phone"})
    @Enum(enumClass = IdentifierType.class, message = "검색 기준값을 입력하세요.", ignoreCase = true)
    private String identifierType;

    @Schema(description = "검색어", example = "test@user.com")
    @NotBlank(message = "계정 검색어를 입력해 주세요.")
    @Size(min = 2, max = 50, message = "2 ~ 50자 이내로 입력해 주세요.")
    private String keyword;
}
