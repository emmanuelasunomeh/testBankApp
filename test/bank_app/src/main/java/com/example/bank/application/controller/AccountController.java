package com.example.bank.application.controller;

import com.example.bank.application.dto.DepositDTO;
import com.example.bank.application.dto.ResponseDTO;
import com.example.bank.application.dto.WithdrawalDTO;
import com.example.bank.application.services.AccountService;
import com.example.bank.application.services.AuthService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Data
@Slf4j
@RestController
@RequestMapping("/api/acc")
public class AccountController {


    @Autowired
    protected AccountService accountService;

    @GetMapping(path ="/account_info/{accountNumber}")
    public ResponseDTO accountInfo(@PathVariable String accountNumber){
        ResponseDTO numberDetails = accountService.fetchAccountNumberDetails(accountNumber);
        return numberDetails;
    }


    @GetMapping(path ="/account_statement/{accountNumber}")
    public List<?> accountStatement(@PathVariable String accountNumber){
        List<?> transactionDetails = accountService.fetchTransactionRecordByAccountNumber(accountNumber);
        return transactionDetails;
    }

    @Transactional
    @PostMapping(path ="/deposit")
    public ResponseDTO deposit(@RequestBody DepositDTO deposit){
        ResponseDTO savedDeposit = accountService.deposit(deposit);
        return savedDeposit;
    }

    @Transactional
    @PostMapping(path ="/withdrawal")
    public ResponseDTO withdrawal(@RequestBody WithdrawalDTO withdrawal){
        ResponseDTO savedWithdrawal = accountService.withdrawal(withdrawal);
        return savedWithdrawal;
    }
}
