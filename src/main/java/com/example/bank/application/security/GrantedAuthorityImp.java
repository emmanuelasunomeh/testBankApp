package com.example.bank.application.security;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImp implements GrantedAuthority {

    public String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
