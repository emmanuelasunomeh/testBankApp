package com.example.bank.application.services;

import com.example.bank.application.dto.DepositDTO;
import com.example.bank.application.dto.ResponseDTO;
import com.example.bank.application.dto.WithdrawalDTO;
import com.example.bank.application.dto.response.JsonResponse;
import com.example.bank.application.enumeration.TransactionType;
import com.example.bank.application.model.Account;
import com.example.bank.application.model.Account_User;
import com.example.bank.application.model.Transactions;
import com.example.bank.application.repository.AccountRepository;
import com.example.bank.application.repository.AccountUserRepository;
import com.example.bank.application.repository.TransactionsRepository;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.doReturn;

/**
 * @author: Emmanuel Asunomeh
 * @Date: 8/22/2021
 */
@SpringBootTest
@Data
public class AccountTestService {

    @MockBean
    private AccountService service;

    /**
     * Create a mock implementation of all Necessary Repository
     */
    @MockBean
    private AccountUserRepository accountUserRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TransactionsRepository transactionsRepository;

    static final Account_User userAccount = new Account_User(1L,"Emmanuel", "$2a$10$J9KP2k070SDjXCLNWWM/M.OxT5g0r735uchseBCZAtnxMhyjfNEU2", "8165487498");


    @Test
    @DisplayName("Test Account findById Success")
    void testAccountFindById() {
        // Setup our mock testAccountFindById repository
        Account account = new Account(1l,userAccount,  600D);
        doReturn(Optional.of(account)).when(accountRepository).findById(1l);
        // Execute the service call
        Optional<Account> returnedAccount = accountRepository.findById(1l);
        // Assert the response
        Assertions.assertTrue(returnedAccount.isPresent(), "Account was not found");
        Assertions.assertSame(returnedAccount.get(), account, "The Account returned was not the same as the mock");
    }
    @Test
    @DisplayName("Test fetchAccountNumberDetails Success")
    void testFetchAccountNumberDetails() {
        // Setup our mock repository
        Account account = new Account(1l,userAccount,  9000D);
//        ResponseEntity responseAccount = new ResponseEntity(new JsonResponse("See Data Object for Details!",account),HttpStatus.OK);
        ResponseDTO responseAccount = new ResponseDTO(HttpStatus.OK.value(),true,"See Data Object for Details!", account);


        doReturn(responseAccount).when(service).fetchAccountNumberDetails("8165487498");
        // Execute the service call
        ResponseDTO returnedAccount = service.fetchAccountNumberDetails("8165487498");
        // Assert the response
        Assertions.assertTrue((returnedAccount.getResponseCode()== HttpStatus.OK.value()), "Account was not found");
        Assertions.assertSame(returnedAccount, responseAccount, "The Account returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test fetchTransactionRecordByAccountNumber Success")
    void testFetchTransactionRecordByAccountNumber() {

        // Setup our mock repository
        List<Transactions> transactionList = new ArrayList<>();
        Transactions transaction1 = new Transactions(1L, userAccount,900D, 600D, 900D, new Date(), TransactionType.DEPOSIT.name(), "TESTING 1 DEPOSITING");
        Transactions transaction2 = new Transactions(1L, userAccount,500D, 500D, 500D, new Date(), TransactionType.DEPOSIT.name(), "TESTING 2 DEPOSITING");
        transactionList.addAll(Arrays.asList(transaction1,transaction2));
//        Account account = new Account(1l,userAccount,  9000D);
        doReturn(transactionList).when(transactionsRepository).findByAccountUserAccountNumber("8165487498");
        // Execute the service call
        List<Transactions> returnedAccount = transactionsRepository.findByAccountUserAccountNumber("8165487498");
        // Assert the response
        Assertions.assertTrue((returnedAccount.size()==2), "transactionList was not found");
        Assertions.assertEquals(returnedAccount.size(), 2L);
        Assertions.assertSame(returnedAccount, transactionList, "The transactionList returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test Withdrawal Success")
    void testWithdrawal() {

        // Setup our mock repository
        WithdrawalDTO withdrawalDTO = new WithdrawalDTO(userAccount.getAccountNumber(),userAccount.getPassword(), 900D);
        ResponseDTO responseAccount = new ResponseDTO(HttpStatus.OK.value(),true,"Successful Transaction!");

        doReturn(responseAccount).when(service).withdrawal(withdrawalDTO);
//        doReturn(responseAccount).doThrow(new RuntimeException("Unable to Withdraw")).when(service).withdrawal(withdrawalDTO);

        // Execute the service call
        ResponseDTO returnedAccount = service.withdrawal(withdrawalDTO);
        // Assert the response
        Assertions.assertTrue((returnedAccount.getResponseCode()== HttpStatus.OK.value()), "Account was not found");
        Assertions.assertSame(returnedAccount, responseAccount, "The Account returned was not the same as the mock");
}

    @Test
    @DisplayName("Test Deposit Success")
    void testDeposit() {

        // Setup our mock repository
        DepositDTO depositDTO = new DepositDTO(userAccount.getAccountNumber(),600D);

        ResponseDTO responseAccount = new ResponseDTO(HttpStatus.OK.value(),true,"Successful Transaction!");


        doReturn(responseAccount).when(service).deposit(depositDTO);
        // Execute the service call
        ResponseDTO returnedAccount = service.deposit(depositDTO);
        // Assert the response
        Assertions.assertTrue((returnedAccount.getResponseCode()== HttpStatus.OK.value()), "Account was not found");
        Assertions.assertSame(returnedAccount, responseAccount, "The Account returned was not the same as the mock");
}
}
