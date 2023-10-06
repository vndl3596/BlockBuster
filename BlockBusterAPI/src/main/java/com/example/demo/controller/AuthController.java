package com.example.demo.controller;

import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.MailException;
import com.example.demo.exception.UsernameExitException;
import com.example.demo.model.Account;
import com.example.demo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private ArrayList<String> loginList = new ArrayList<>();

    @PostMapping("/signup")
    public ResponseEntity<Account> signup(@RequestBody RegisterRequest registerRequest) throws UsernameExitException, MailException {
        return new ResponseEntity<>(authService.signup(registerRequest), HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authenticationResponse = authService.login(loginRequest);
        if(authenticationResponse.getAccId() > 0){
            loginList.add(authenticationResponse.getUsername());
        }
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @GetMapping("/isLogin/{username}")
    public ResponseEntity<Integer> verifyIsLoginAccount(@PathVariable String username) {
        if(loginList.contains(username)){
            return new ResponseEntity<>(1, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }

    @GetMapping("/logout/{username}")
    public ResponseEntity<String> logout(@PathVariable String username) {
        loginList.remove(username);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
