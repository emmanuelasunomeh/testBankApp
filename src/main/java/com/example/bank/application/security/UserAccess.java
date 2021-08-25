
package com.example.bank.application.security;

import com.example.bank.application.model.Account_User;

import java.util.ArrayList;

public class UserAccess extends org.springframework.security.core.userdetails.User {
    
    private Account_User user;
    
    public UserAccess(Account_User user) {
        super(user.getAccountName(), user.getPassword(), true, true, true, true, new ArrayList<>());
        this.user = user;
    }

    public Account_User getUser() {
        return this.user;
    }

    public void setUser(Account_User user) {
        this.user = user;
    }
    
}
