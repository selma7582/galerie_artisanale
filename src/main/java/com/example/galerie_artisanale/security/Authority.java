package com.example.galerie_artisanale.security;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class Authority implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 1L;

    private final String auth;

    public Authority(String authority) {
        this.auth = authority;
    }

    @Override
    public String getAuthority() {
        return auth;
    }
}
