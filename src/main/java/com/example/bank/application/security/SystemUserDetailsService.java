package com.example.bank.application.security;


import java.util.ArrayList;

import com.example.bank.application.model.Account_User;
import com.example.bank.application.repository.AccountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SystemUserDetailsService implements UserDetailsService {

    @Autowired
    AccountUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account_User account_user = userRepository.findByUsernameIgnoreCaseOrEmailOrAccountNumber(username).orElse(null);
        if (account_user==null){
            throw new UsernameNotFoundException("User Not Found with -> username or email or Account Number: " + username);
        }
        return new UserAccess(account_user);
    }
    
    
}
