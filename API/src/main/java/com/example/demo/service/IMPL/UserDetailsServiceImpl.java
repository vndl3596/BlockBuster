package com.example.demo.service.IMPL;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findMovieAccountByUsername((username));
        if (account == null) {
            throw new UsernameNotFoundException("User not Found with username: " + username);
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            account.getAccountRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getAccountRole().getName()));
            });
            return new User(
                    account.getUsername(),
                    account.getPassword(),
                    account.isEnabled(),
                    true,
                    true,
                    true,
                    authorities);
        }
    }

}
