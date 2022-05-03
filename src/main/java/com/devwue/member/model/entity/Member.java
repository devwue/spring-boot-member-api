package com.devwue.member.model.entity;


import com.devwue.member.controller.model.request.SignUpRequest;
import com.devwue.member.model.response.MemberDto;
import com.devwue.member.model.response.MemberFullDto;
import com.devwue.member.model.response.MemberSearchDto;
import com.devwue.member.support.StringFilter;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "member")
@AllArgsConstructor
public class Member extends BaseEntity {
    @Column
    private String email;
    @Column
    private String name;

    @Column(name="nick_name")
    private String nickName;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="phone_agency")
    private String phoneAgency;

    @Column(name="phone_validate")
    private Boolean phoneValidate;

    @Column
    private String password;

    @Column(name="sign_in_at")
    private LocalDateTime signInAt;

    public Member() {}

    public Member toMember(SignUpRequest request) {
        return new Member()
                .setEmail(request.getEmail())
                .setName(request.getName())
                .setNickName(request.getNickName())
                .setPhoneAgency(request.getPhoneAgency())
                .setPhoneNumber(StringFilter.onlyNumber(request.getPhoneNumber()))
                .setPhoneValidate(true)
                .setSignInAt(null);
    }

    public MemberDto toMemberDto() {
        return MemberDto.builder()
                .email(this.email)
                .name(this.name)
                .nickName(this.nickName)
                .phoneAgency(this.phoneAgency)
                .phoneNumber(this.phoneNumber)
                .phoneValidate(this.phoneValidate)
                .build();
    }

    public MemberFullDto toMemberFullDto() {
        return MemberFullDto.builder()
                .email(this.email)
                .name(this.name)
                .nickName(this.nickName)
                .phoneAgency(this.phoneAgency)
                .phoneNumber(this.phoneNumber)
                .phoneValidate(this.phoneValidate)
                .signInAt(this.getSignInAt())
                .createdAt(this.getCreatedAt())
                .build();
    }

    public MemberSearchDto toMemberSearchDto() {
        return MemberSearchDto.builder()
                .email(this.email)
                .name(this.name)
                .nickName(this.nickName)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}
