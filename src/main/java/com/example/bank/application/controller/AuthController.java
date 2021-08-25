package com.example.bank.application.controller;

import com.example.bank.application.dto.AccountUserDto;
import com.example.bank.application.dto.LoginRequestDto;
import com.example.bank.application.dto.ResponseDTO;
import com.example.bank.application.security.jwt.JwtProvider;
import com.example.bank.application.services.AuthService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Data
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    protected AuthService authService;

    @GetMapping(path = "/welcome")
    public ResponseEntity<?> welcomePage(
    ) {
        return ResponseEntity.ok("WELCOME TO THE BANK APPLICATION");
    }

    @Transactional
    @PostMapping(path = "/create_account")
    public ResponseDTO createUserAccount(@Valid @RequestBody AccountUserDto accountUserDto,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        ResponseDTO user = authService.createAccountUser(accountUserDto);
        return user;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> authenticateUser(
            HttpServletRequest httpRequest,
            @RequestBody LoginRequestDto loginRequestDto
    ) {
        ResponseEntity<?> login = authService.login(loginRequestDto);
        return login;
    }

    @GetMapping(path = "/logout/{status}")
    public ResponseEntity<?> logoutAuthenticatedUser(
            HttpServletRequest httpRequest,
            @PathVariable(required=false) AuthService.LoginStatus status
    ) {
        jwtProvider.clearClaims(httpRequest);
        return ResponseEntity.ok(true);
    }

    




}
