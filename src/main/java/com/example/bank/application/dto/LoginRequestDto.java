
package com.example.bank.application.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    
    private Long accountNumber;
    
    private String password;
}
