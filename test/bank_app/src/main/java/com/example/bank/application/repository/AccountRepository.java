package com.example.bank.application.repository;

import com.example.bank.application.model.Account;
import com.example.bank.application.model.Account_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountUserAccountNumber(String accountNumber);
}
