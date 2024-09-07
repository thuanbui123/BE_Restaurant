package com.example.restaurant.repository;

import com.example.restaurant.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Integer> {
    Optional<AccountInfo> findByUsername (String username);
    Optional<AccountInfo> findByUsernameAndPassword(String username, String password);
}
