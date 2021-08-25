package com.example.bank.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author: Emmanuel Asunomeh
 * @Date: 8/22/2021
 */

@AllArgsConstructor
@Data
public class TransactionDTO {
    Date transactionDate;
    String transactionType;
    String narration;
    Double amount;
    Double accountBalance;
}
