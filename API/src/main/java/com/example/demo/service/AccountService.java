package com.example.demo.service;


import com.example.demo.DTO.AccountDTO;
import com.example.demo.DTO.AccountPage;
import com.example.demo.exception.MailException;
import com.example.demo.exception.UsernameExitException;
import com.example.demo.model.Account;
import com.example.demo.model.AccountRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAllAccounts();

    List<AccountDTO> getAccountByEnabled(boolean check);

    List<AccountRole> getFullRoleOnAccount(Account account);

    AccountDTO getAccountById(int accountId);

    AccountDTO getAccountByUsernameDTO(String accountUsername);

    Account getAccountByUsername(String accountUsername);

    boolean checkPasswordForAccount(Account account, String currentPassword);

    Boolean changePasswordForAccount(Account account, String passwordChange);

    String deleteAccountByUsername(String username);

    AccountDTO editAccountByUsername(AccountDTO account) throws UsernameExitException, MailException;

    Account createAccount(Account account) throws MailException, UsernameExitException;

    AccountPage getAllUsersPage(int pageNo, int pageSize, String sortBy, String sortDir);

    boolean forgotPassword(Account account);

    Account getAccountByEmail(String email);

    boolean saveImageAcc(MultipartFile image, int accId);
}
