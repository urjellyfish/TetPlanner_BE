package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    Page<Budget> getByUserIdAndIsDeletedFalse(UUID userId, Pageable pageable);

    Optional<Budget> findByIdAndUserIdAndIsDeletedFalse(UUID id, UUID userId);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Budget b WHERE b.user.id = :userId AND b.isDeleted = false")
    long sumTotalAmountByUserIdAndIsDeletedFalse(UUID userId);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Budget b WHERE b.user.id = :userId AND b.occasion.id = :occasionId AND b.isDeleted = false")
    long sumTotalAmountByUserIdAndOccasionId(@Param("userId") UUID userId, @Param("occasionId") UUID occasionId);
}
