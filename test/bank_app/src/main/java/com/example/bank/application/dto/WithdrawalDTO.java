package com.example.bank.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Emmanuel Asunomeh
 * @Date: 8/22/2021
 */

@AllArgsConstructor
@Data
public class WithdrawalDTO {

    String accountNumber;
    String accountPassword;
    Double withdrawnAmount;
}
