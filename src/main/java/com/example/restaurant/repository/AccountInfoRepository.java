package com.example.restaurant.repository;

import com.example.restaurant.entity.AccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Integer> {
    Optional<AccountInfo> findByUsername (String username);

    @Query(value = "select * from account where id = :id", nativeQuery = true)
    AccountInfo findOneById (@Param("id") Integer id);
    @Query(value = "select * from account where slug = :slug", nativeQuery = true)
    AccountInfo findOneBySlug(@Param("slug") String slug);

    List<AccountInfo> findByRole(String role);

    Page<AccountInfo> findByRoleNot(String role, Pageable pageable);

    @Query(value = "select count(*) from account where role = 'ROLE_USER' AND created_at between :startDate and :endDate", nativeQuery = true)
    Long countNewAccountsByRoleUserBetweenDates (@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    Page<AccountInfo> findBySlugContainingIgnoreCase (String slug, Pageable pageable);
}
