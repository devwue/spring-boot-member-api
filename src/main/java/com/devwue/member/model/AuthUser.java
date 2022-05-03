package com.devwue.member.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUser extends User {
    public AuthUser(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, true, true, true, true, authorities);
    }
}
