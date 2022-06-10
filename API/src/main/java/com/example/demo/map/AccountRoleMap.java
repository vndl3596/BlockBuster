package com.example.demo.map;

import com.example.demo.DTO.AccountRoleDTO;
import com.example.demo.model.AccountRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountRoleMap {
    public AccountRoleDTO accountRoleToDTO(AccountRole accountRole) {
        return new AccountRoleDTO(accountRole.getId(), accountRole.getName());
    }

    public List<AccountRoleDTO> listAccountToDTO(List<AccountRole> accountRoles) {
        List<AccountRoleDTO> accountRoleDTOS = new ArrayList<>();
        accountRoles.forEach(accountRole -> {
            accountRoleDTOS.add(accountRoleToDTO(accountRole));
        });
        return accountRoleDTOS;
    }
}
