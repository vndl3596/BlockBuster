package com.example.demo.controller;

import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.AccountPage;
import com.example.demo.dto.Password;
import com.example.demo.exception.MailException;
import com.example.demo.exception.UsernameExitException;
import com.example.demo.model.Account;
import com.example.demo.model.utils.PagingHeaders;
import com.example.demo.model.utils.PagingResponse;
import com.example.demo.service.AccountService;
import com.example.demo.service.ImageService;
import com.example.demo.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/acc")
public class AccountController {
    private final ImageService imageService;
    private final AccountService accountService;

    @PostMapping("/createAcc")
    public ResponseEntity<AccountDTO> createNewAccount(@RequestBody AccountDTO account) throws UsernameExitException, MailException {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    @GetMapping("/getAccoutByUsername/{username}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String username) {
        return new ResponseEntity<>(accountService.getAccountByUsernameDTO(username), HttpStatus.OK);
    }
    @GetMapping("/getAllAccount")
    public ResponseEntity<List<AccountDTO>> getAllAccount() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }
    @GetMapping("/getAccById/{accId}")
    public ResponseEntity<AccountDTO> getAccById(@PathVariable("accId") int id){
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<AccountDTO> editAccountByUsername(@RequestBody AccountDTO account) throws UsernameExitException, MailException {

        return new ResponseEntity<>(accountService.editAccountByUsername(account), HttpStatus.OK);
    }

    @GetMapping("/deleteAcc/{username}")
    public ResponseEntity<?> deleteAccountByUsername(@PathVariable String username) {
        return new ResponseEntity<>(accountService.deleteAccountByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/deactiveAcc/{username}")
    public ResponseEntity<?> deActiveAccountByUsername(@PathVariable String username) {
        return new ResponseEntity<>(accountService.deActiveAccountByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/checkPassword/{username}")
    public ResponseEntity<Boolean> checkPassword(@RequestBody Password password, @PathVariable String username) {
        Account account = accountService.getAccountByUsername(username);
        return new ResponseEntity<>(accountService.checkPasswordForAccount(account, password.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/changePassword/{username}")
    public ResponseEntity<Boolean> changePassword(@RequestBody Password password, @PathVariable String username) {
        Account account = accountService.getAccountByUsername(username);
        return new ResponseEntity<>(accountService.changePasswordForAccount(account, password.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<Boolean> forgotPassword(@PathVariable String email) {
        Account account = accountService.getAccountByEmail(email);
        return new ResponseEntity<>(accountService.forgotPassword(account), HttpStatus.OK);
    }

    public HttpHeaders returnHttpHeaders(PagingResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
        headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
        headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffset()));
        headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
        headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
        return headers;
    }
}
