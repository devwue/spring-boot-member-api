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
    @Column(nullable = false)
    private String feature;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "phone_token", nullable = false)
    private String phoneToken;

    @Column(nullable = false)
    private Integer status;
}
