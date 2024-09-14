package com.example.restaurant.repository;

import com.example.restaurant.entity.AccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Integer> {
    Optional<AccountInfo> findByUsername (String username);

    @Query(value = "select * from account where id = :id", nativeQuery = true)
    AccountInfo findOneById (@Param("id") Integer id);

    AccountInfo findOneBySlug(String slug);

    Page<AccountInfo> findByRole(String role, Pageable pageable);

    Page<AccountInfo> findBySlugContainingIgnoreCaseAndRole (String slug, String role, Pageable pageable);
}
