package com.example.bank.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="TRANSACTIONS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_USER_FK", referencedColumnName = "id")
    private Account_User accountUser;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "INITIAL_DEPOSIT")
    private Double initialDeposit;

    @Column(name = "ACCOUNT_BALANCE")
    private Double accountBalance;

    @Column(name = "TRANSACTION_DATE")
    private Date transactionDate = new Date();

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "NARRATION")
    private String narration;
}
