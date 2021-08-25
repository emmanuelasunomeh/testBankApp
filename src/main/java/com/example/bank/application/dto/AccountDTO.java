package com.example.bank.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author: Emmanuel Asunomeh
 * @Date: 8/22/2021
 */

@AllArgsConstructor
@Data
public class AccountDTO {
    private String accountName;
    private String accountNumber;
    private Double balance;

}
