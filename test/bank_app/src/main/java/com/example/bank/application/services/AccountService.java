package com.example.bank.application.services;


import com.example.bank.application.dto.*;
import com.example.bank.application.dto.response.JsonResponse;
import com.example.bank.application.enumeration.TransactionType;
import com.example.bank.application.model.Account;
import com.example.bank.application.model.Transactions;
import com.example.bank.application.repository.AccountRepository;
import com.example.bank.application.repository.AccountUserRepository;
import com.example.bank.application.repository.TransactionsRepository;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Emmanuel Asunomeh
 * @Date: 8/22/2021
 */

@Data
@Service
public class AccountService {

    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;
    private final TransactionsRepository transactionsRepository;

    public ResponseDTO fetchAccountNumberDetails(String accountNumber) {
        Account account  = accountRepository.findByAccountUserAccountNumber(accountNumber).orElse(null);
        if(account==null){
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value());
        }
        AccountDTO accountDto = new AccountDTO(account.getAccountUser().getAccountName(),
                account.getAccountUser().getAccountNumber(), account.getBalance());

        return new ResponseDTO(HttpStatus.OK.value(),true,"See Data Object for Details!", accountDto);
    }

    public List<?> fetchTransactionRecordByAccountNumber(String accountNumber) {
        List<Transactions> transactions  = transactionsRepository.findByAccountUserAccountNumber(accountNumber);

        List<TransactionDTO> transactionList = transactions.stream().map(transaction ->{
            TransactionDTO transactionDTO = new TransactionDTO(transaction.getTransactionDate(),
                    transaction.getTransactionType(), transaction.getNarration(),
                    transaction.getAmount(), transaction.getAccountBalance());
            return transactionDTO;
        }).collect(Collectors.toList());

        return transactionList;
    }

    public ResponseDTO deposit(DepositDTO depositDTO) {

        if(depositDTO.getAmount()>1_000_000D || depositDTO.getAmount()<1D){
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value());
        }

        Transactions findOneTransaction = transactionsRepository
                .findLastByAccountUserAccountNumber(depositDTO.getAccountNumber())
                .orElse(null);
        Account account  = accountRepository.findByAccountUserAccountNumber(depositDTO.getAccountNumber()).orElse(null);

        Double balance = 0D;
        if(findOneTransaction!=null){
             balance = findOneTransaction.getAccountBalance() + depositDTO.getAmount();
        }

        Transactions transaction = new Transactions();
        transaction.setNarration("DEPOSITING "+depositDTO.getAmount() + "");
        transaction.setAmount(depositDTO.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT.name());
        transaction.setAccountUser(findOneTransaction.getAccountUser());
        transaction.setAccountBalance(balance);
        Transactions savedTransactions = transactionsRepository.save(transaction);

        if(savedTransactions!=null && account!=null){
            account.setBalance(balance);
            accountRepository.save(account);
        }

        return new ResponseDTO(HttpStatus.OK.value(),true,"Successful Transaction!");
    }

    public ResponseDTO withdrawal(WithdrawalDTO withdrawal) {

        Transactions findOneTransaction = transactionsRepository
                .findLastByAccountUserAccountNumber(withdrawal.getAccountNumber())
                .orElse(null);
        Account account  = accountRepository.findByAccountUserAccountNumber(withdrawal.getAccountNumber()).orElse(null);

        Double balance = 0D;
        if(findOneTransaction!=null){
            balance = findOneTransaction.getAccountBalance() - withdrawal.getWithdrawnAmount();
        }
        if(withdrawal.getWithdrawnAmount() < 1D || balance < 500D){
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Rejecting Transaction with current amount less than 500.00 or withdrawal less than 1.0 !");
        }

        Transactions transaction = new Transactions();
        transaction.setNarration("WITHDRAWING "+withdrawal.getWithdrawnAmount() + "");
        transaction.setAmount(withdrawal.getWithdrawnAmount());
        transaction.setTransactionType(TransactionType.WITHDRAWAL.name());
        transaction.setAccountUser(findOneTransaction.getAccountUser());
        transaction.setAccountBalance(balance);
        Transactions savedTransactions = transactionsRepository.save(transaction);

        if(savedTransactions!=null && account!=null){
            account.setBalance(balance);
            accountRepository.save(account);
        }
        return new ResponseDTO(HttpStatus.OK.value(),true,"Successful Transaction!");

    }


}


