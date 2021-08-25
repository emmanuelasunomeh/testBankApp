package com.example.bank.application.repository;

import com.example.bank.application.model.Account_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountUserRepository extends JpaRepository<Account_User, Long> {


    @Query(value = "SELECT * from Account_User "
            + "where ACCOUNT_NUMBER =:accountNumber ", nativeQuery = true)
    Optional<Account_User> findByUsernameIgnoreCaseOrEmailOrAccountNumber(@Param("accountNumber") String accountNumber);

    Boolean existsByAccountNumber(String accountNumber);
    Boolean existsByAccountName(String name);
}
