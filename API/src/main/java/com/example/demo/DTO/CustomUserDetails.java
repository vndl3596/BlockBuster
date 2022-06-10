package com.example.demo.DTO;

import com.example.demo.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Account account;

    public static CustomUserDetails build(Account user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getAccountRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getAccountRole().getName()));
        });
        return new CustomUserDetails(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        account.getAccountRoles().forEach(role -> {
            System.out.println("\n\n Name role: " + role.getAccountRole().getName());
            authorities.add(new SimpleGrantedAuthority(role.getAccountRole().getName()));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.isEnabled();
    }
}
