package com.devwue.member.controller.model.request;

import com.devwue.member.annotation.Enum;
import com.devwue.member.model.enums.IdentifierType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "계정 검색 요청 모델")
@Getter
@AllArgsConstructor
public class SearchRequest {
    @Schema(description = "계정 검색 기준", example = "email", allowableValues = {"email", "nick", "phone"})
    @Enum(enumClass = IdentifierType.class, message = "{identifier-type.enum}", ignoreCase = true)
    private String identifierType;

    @Schema(description = "검색어", example = "test@user.com")
    @NotBlank(message = "{account-search.notblank}")
    @Size(min = 2, max = 50, message = "{account-search.size}")
    private String keyword;
}
