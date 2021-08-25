package com.example.bank.application.services;


import com.example.bank.application.dto.AccountUserDto;
import com.example.bank.application.dto.LoginRequestDto;
import com.example.bank.application.dto.LoginResponseDto;
import com.example.bank.application.dto.ResponseDTO;
import com.example.bank.application.dto.response.JsonResponse;
import com.example.bank.application.enumeration.TransactionType;
import com.example.bank.application.model.Account;
import com.example.bank.application.model.Account_User;
import com.example.bank.application.model.Transactions;
import com.example.bank.application.repository.AccountRepository;
import com.example.bank.application.repository.AccountUserRepository;
import com.example.bank.application.repository.TransactionsRepository;
import com.example.bank.application.security.UserAccess;

import com.example.bank.application.security.jwt.JwtProvider;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Data
@Service
public class AuthService {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;
    private final TransactionsRepository transactionsRepository;



    public ResponseDTO createAccountUser(AccountUserDto accountUserDto) {
        Double initialDeposit = accountUserDto.getInitialDeposit();

        if(initialDeposit < 500){
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Less than the Minimum Amount!!");
        }
        Account_User accountUser = new Account_User();
//        CHECK FOR EXISTING ACCOUNT BY EMAIL
        String name = accountUserDto.getAccountName();
        Boolean isAccountNamePresent = accountUserRepository.existsByAccountName(name);

        if(isAccountNamePresent.equals(Boolean.TRUE)){
            return new ResponseDTO(HttpStatus.CONFLICT.value(), "Account Name Already Exist!!");
        }

        accountUser.setAccountName(accountUserDto.getAccountName());
        accountUser.setPassword(bCryptPasswordEncoder.encode(accountUserDto.getAccountPassword()));
        String accountNumber = this.generateAccountNumber().toString();
        accountUser.setAccountNumber(accountNumber);
        accountUser = accountUserRepository.save(accountUser);

        Account account = new Account();
        account.setAccountUser(accountUser);
        account.setBalance(initialDeposit);
        accountRepository.save(account);

        Transactions transactions = new Transactions();
        transactions.setAccountUser(accountUser);
        transactions.setInitialDeposit(initialDeposit);
        transactions.setAccountBalance(initialDeposit);
        transactions.setTransactionType(TransactionType.DEPOSIT.name());
        transactions.setAmount(initialDeposit);
        transactions.setNarration("FIRST DEPOSIT");
        transactionsRepository.save(transactions);

        return new ResponseDTO(HttpStatus.OK.value(),true, "Registration Was Successful!");
    }



    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getAccountNumber(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserAccess userAccess = (UserAccess) authentication.getPrincipal();
        Account_User accountUser = userAccess.getUser();
        if (accountUser == null) {
            return ResponseEntity.ok(new JsonResponse(HttpStatus.NOT_FOUND, "Invalid Username and/or Password"));
        }
        String jwt = jwtProvider.generateJwtToken(authentication, accountUser.getId());


        return ResponseEntity.ok(new LoginResponseDto(jwt));
    }



    public static enum LoginStatus {
        LOGIN,
        LOGGED_OUT,
        EXPIRED,
        ACTIVE,
        INACTIVE,
        DEACTIVATED;

        public static boolean validateStatus(String status){
            for(LoginStatus loginStatus : LoginStatus.values()){
                if (loginStatus.name().equalsIgnoreCase(status)){
                    return true;
                }
            }
            return false;
        };
    }


    @NotNull
    private Long generateAccountNumber() {
        Long accountNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        Boolean isNumberPresent = accountUserRepository.existsByAccountNumber(accountNumber.toString());
        Long count = 0L;
        while(isNumberPresent){
            accountNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
            isNumberPresent = accountUserRepository.existsByAccountNumber(accountNumber.toString());
            count++;
            if(count ==6){ isNumberPresent=false; }
        }

        if(isNumberPresent.equals(false) && count.equals(6)){
            throw new RuntimeException("Unable to Generate Account Number, Please Try Again Later!!");
        }
        return accountNumber;
    }

}


