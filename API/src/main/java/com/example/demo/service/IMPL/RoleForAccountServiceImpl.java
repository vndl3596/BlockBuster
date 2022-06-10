package com.example.demo.service.IMPL;

import com.example.demo.DTO.AccountRoleDTO;
import com.example.demo.map.AccountRoleMap;
import com.example.demo.model.AccountRole;
import com.example.demo.model.Key.RoleForAccountKey;
import com.example.demo.model.Account;
import com.example.demo.model.RoleForAccount;
import com.example.demo.repository.RoleForAccountRepository;
import com.example.demo.service.RoleForAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleForAccountServiceImpl implements RoleForAccountService {
    private final RoleForAccountRepository roleForAccountRepository;
    private final AccountRoleMap accountRoleMap;

    @Override
    public void addRoleForAccount(Account account, AccountRole accountRole) {
        RoleForAccountKey roleForAccountKey = new RoleForAccountKey();
        roleForAccountKey.setAccountId(account.getId());
        roleForAccountKey.setRoleId(accountRole.getId());
        RoleForAccount roleForAccount = new RoleForAccount();
        roleForAccount.setId(roleForAccountKey);
        roleForAccount.setAccountRole(accountRole);
        roleForAccount.setAccount(account);
        roleForAccountRepository.save(roleForAccount);
    }

    @Override
    public List<AccountRoleDTO> getRoleForAccount(int accId) {
        List<AccountRole> accountRoles = new ArrayList<>();
        List<RoleForAccount> roleForAccounts = roleForAccountRepository.findAll();
        for (RoleForAccount roleForAccount: roleForAccounts) {
            if (roleForAccount.getId().getAccountId() == accId){
                accountRoles.add(roleForAccount.getAccountRole());
            }
        }

        return accountRoleMap.listAccountToDTO(accountRoles);
    }

    @Override
    public void deleteRole(int userId) {
        List<RoleForAccount> roleForAccounts = roleForAccountRepository.findAll();
        roleForAccounts.forEach(roleForAccount -> {
            if (roleForAccount.getAccount().getId() == userId) {
                roleForAccountRepository.delete(roleForAccount);
            }
        });
    }
}
