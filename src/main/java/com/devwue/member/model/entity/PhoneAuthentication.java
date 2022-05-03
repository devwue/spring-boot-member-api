package com.devwue.member.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "phone_authentication")
public class PhoneAuthentication extends BaseEntity {
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_token")
    private String phoneToken;

    private Integer status;
}
