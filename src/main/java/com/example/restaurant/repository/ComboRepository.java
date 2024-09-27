package com.example.restaurant.repository;

import com.example.restaurant.entity.ComboEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends JpaRepository<ComboEntity, Integer> {
    Page<ComboEntity> findBySlugContainingIgnoreCase(String slug, Pageable pageable);

    boolean existsByCode(String code);

    @Modifying
    @Transactional
    @Query("UPDATE ComboEntity c SET c.status = 'Đang áp dụng' WHERE c.validFrom <= CURRENT_TIMESTAMP AND (c.validTo IS NULL OR c.validTo > CURRENT_TIMESTAMP) AND c.status = 'Chưa áp dụng'")
    void activateCombos();

    @Modifying
    @Transactional
    @Query("UPDATE ComboEntity c SET c.status = 'Đã hết hạn' WHERE c.validTo <= CURRENT_TIMESTAMP AND c.status = 'Đang áp dụng'")
    void deactivateExpiredCombos();

    @Query(value = "select * from combo where id = :id", nativeQuery = true)
    ComboEntity findOneById (@Param("id") Integer id);

    ComboEntity findOneByCode(String code);

    void deleteByCode(String code);
}
