package com.akasmiou.ouassima.EBanking.security.repositories;

import com.akasmiou.ouassima.EBanking.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findAppRoleByRoleName(String username);
}
