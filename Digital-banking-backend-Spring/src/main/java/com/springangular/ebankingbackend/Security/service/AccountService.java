package com.springangular.ebankingbackend.Security.service;

import com.springangular.ebankingbackend.Security.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
}
