package com.example.bank.application.repository;


import com.example.bank.application.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    List<Transactions> findByAccountUserAccountNumber(String accountNumber);

    @Query(value = "SELECT T.* from TRANSACTIONS T " +
            " LEFT JOIN ACCOUNT_USER A  ON T.ACCOUNT_USER_FK = A.ID "
            + "where A.ACCOUNT_NUMBER =:accountNumber ORDER BY T.ID DESC LIMIT 1 ", nativeQuery = true)
    Optional<Transactions> findLastByAccountUserAccountNumber(@Param("accountNumber")String accountNumber);


}
