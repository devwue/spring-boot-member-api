package com.devwue.member.model.entity;


import com.devwue.member.controller.model.request.SignUpRequest;
import com.devwue.member.support.StringFilter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity(name = "member")
@AllArgsConstructor
public class Member extends BaseEntity {
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name="nick_name", nullable = false)
    private String nickName;

    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

    @Column(name="phone_agency", nullable = false)
    private String phoneAgency;

    @Column(name="phone_validate", nullable = false)
    private Boolean phoneValidate;

    @Column(nullable = false)
    private String password;

    @Column(name="sign_in_at")
    private LocalDateTime signInAt;

    protected Member() {}

    @Builder
    private Member(SignUpRequest signUpRequest) {
        this.email = signUpRequest.getEmail();
        this.nickName = signUpRequest.getNickName();
        this.phoneAgency = signUpRequest.getPhoneAgency();
        this.phoneNumber = StringFilter.onlyNumber(signUpRequest.getPhoneNumber());
        this.phoneValidate = true;
        this.signInAt = null;
    }

    public Member setName(String name) {
        this.name = name;
        return this;
    }

    public Member setPassword(String password) {
        this.password = password;
        return this;
    }

    public void setSignInAt() {
        this.signInAt = LocalDateTime.now();
    }
}
