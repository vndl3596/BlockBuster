package com.example.demo.service;

import com.example.demo.dto.AccountRoleDTO;
import com.example.demo.model.AccountRole;

import java.util.List;

public interface AccountRoleService {
    List<AccountRole> getAllAccountRole();
    List<AccountRoleDTO> getAccountRolesForUsername(String username);
}
