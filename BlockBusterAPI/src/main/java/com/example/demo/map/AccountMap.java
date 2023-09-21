package com.example.demo.map;

import com.example.demo.dto.AccountDTO;
import com.example.demo.model.Account;
import com.example.demo.model.RoleForAccount;
import com.example.demo.repository.address.TownRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class AccountMap {
    private final TownRepository townRepository;

    public AccountDTO accountToDTO(Account account) {
        List<String> roles = new ArrayList<>();
        for (RoleForAccount roleForAccount : account.getAccountRoles()) {
            roles.add(roleForAccount.getAccountRole().getName().substring(5).toLowerCase(Locale.ROOT));
        }
        return new AccountDTO(
                account.getId(),
                account.getUsername(),
                account.getPassword(),
                account.isEnabled(),
                account.getEmail(),
                account.getAvatar(),
                account.getFirstname(),
                account.getLastname(),
                account.getBirthday(),
                account.getIdTown().getId(),
                account.getAddress(),
                account.getPhoneNumber(),
                account.isGender(),
                roles);
    }

    public Account DTOToAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setUsername(accountDTO.getUsername());
        account.setPassword(accountDTO.getPassword());
        account.setEnabled(accountDTO.isEnabled());
        account.setEmail(accountDTO.getEmail());
        account.setAvatar(accountDTO.getAvatar());
        account.setFirstname(accountDTO.getFirstname());
        account.setLastname(accountDTO.getLastname());
        account.setBirthday(accountDTO.getBirthday());
        account.setIdTown(townRepository.getById(accountDTO.getTown()));
        account.setAddress(accountDTO.getAddress());
        account.setPhoneNumber(accountDTO.getPhoneNumber());
        account.setGender(accountDTO.isGender());
        return account;
    }

    public List<AccountDTO> listAccountToListDTO(List<Account> accounts) {
        List<AccountDTO> accountDTOS = new ArrayList<>();
        for (Account account : accounts) {
            accountDTOS.add(this.accountToDTO(account));
        }
        return accountDTOS;
    }
}
