package com.akasmiou.ouassima.EBanking.security.repositories;

import com.akasmiou.ouassima.EBanking.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findAppUserByUsername(String username);
}
