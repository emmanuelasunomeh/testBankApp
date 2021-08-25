package com.example.bank.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ACCOUNT_USER")
@AllArgsConstructor
@NoArgsConstructor
public class Account_User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

}
