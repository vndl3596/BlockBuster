package com.example.demo.model;

import com.example.demo.model.Key.RoleForAccountKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role_for_account")
public class RoleForAccount {
    @EmbeddedId
    private RoleForAccountKey id = new RoleForAccountKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "id_acc")
    private Account account = new Account();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "id_role")
    private AccountRole accountRole = new AccountRole();
}
