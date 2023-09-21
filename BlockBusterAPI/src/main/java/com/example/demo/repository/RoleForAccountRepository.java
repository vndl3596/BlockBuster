package com.example.demo.repository;

import com.example.demo.model.Key.RoleForAccountKey;
import com.example.demo.model.RoleForAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleForAccountRepository extends JpaRepository<RoleForAccount, RoleForAccountKey> {
}
