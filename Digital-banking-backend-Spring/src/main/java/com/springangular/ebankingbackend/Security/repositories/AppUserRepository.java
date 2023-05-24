package com.springangular.ebankingbackend.Security.repositories;


import com.springangular.ebankingbackend.Security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}