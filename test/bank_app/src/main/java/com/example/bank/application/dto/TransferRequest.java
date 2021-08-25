package com.example.bank.application.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private long from;

    private long to;

    private long amount;

}
