package com.example.demo.controller;

import com.example.demo.DTO.AccountDTO;
import com.example.demo.model.utils.CheckEnabled;
import com.example.demo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class testController {
    private final AccountService accountService;

    @GetMapping("/getAcc")
    public ResponseEntity<List<AccountDTO>> getAllAcc() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }
}
