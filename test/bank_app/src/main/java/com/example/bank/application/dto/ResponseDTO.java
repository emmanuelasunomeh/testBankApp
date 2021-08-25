package com.example.bank.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author: Emmanuel Asunomeh
 * @Date: 8/22/2021
 */


@Data
public class ResponseDTO {


    public ResponseDTO(int responseCode, boolean successful, String message) {
        this.responseCode = responseCode;
        this.successful = successful;
        this.message = message;
    }
    public ResponseDTO(int responseCode, boolean successful) {
        this.responseCode = responseCode;
        this.successful = successful;
    }
    public ResponseDTO(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }
    public ResponseDTO(int responseCode) {
        this.responseCode = responseCode;
    }
    public ResponseDTO(int responseCode, boolean successful, String message, Object account) {
        this.responseCode = responseCode;
        this.successful = successful;
        this.message = message;
        this.account = account;
    }


    int responseCode;
    boolean successful;
    String message;
    Object account;


}


