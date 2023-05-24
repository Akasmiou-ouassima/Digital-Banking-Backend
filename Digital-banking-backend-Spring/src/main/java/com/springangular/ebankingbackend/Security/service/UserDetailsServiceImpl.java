package com.springangular.ebankingbackend.Security.service;

import com.springangular.ebankingbackend.Security.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private AccountService securityService1;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = securityService1.loadUserByUsername(username);
        Collection<GrantedAuthority> authorities1= Collections.singleton(new SimpleGrantedAuthority(appUser.getRole()));
        User user=new User(appUser.getUsername(), appUser.getPassword(), authorities1);
        return user;
    }
}
