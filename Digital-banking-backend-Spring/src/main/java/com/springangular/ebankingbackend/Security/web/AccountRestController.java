package com.springangular.ebankingbackend.Security.web;

import com.springangular.ebankingbackend.Security.entities.AppUser;
import com.springangular.ebankingbackend.Security.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountRestController {
    AccountService accountService;

    @GetMapping("/")
    public List<AppUser> appUsers(){
        return accountService.listUsers();
    }
}
