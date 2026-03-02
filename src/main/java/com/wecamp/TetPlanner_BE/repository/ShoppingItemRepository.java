package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.ShoppingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, UUID> {

    List<ShoppingItem> findByBudgetId(UUID budgetId);

    List<ShoppingItem> findByBudget_IdIn(List<UUID> budgetIds);

    Page<ShoppingItem> findByUserId(UUID userId, Pageable pageable);

    Optional<ShoppingItem> findByIdAndUserId(UUID id, UUID userId);

    @Query("""
    SELECT s
    FROM ShoppingItem s
    JOIN s.user u
    JOIN s.budget b
    WHERE u.id = :userId
    AND b.id = :budgetId
    AND b.isDeleted = false
""")
    Page<ShoppingItem> findAllByUserIdAndBudgetId(
            UUID userId,
            UUID budgetId,
            Pageable pageable);

    @Query("SELECT COUNT(t) FROM ShoppingItem t WHERE t.user.id = :userId ")
    long countByUserId(UUID userId);

    @Query("SELECT COUNT(t) FROM ShoppingItem t WHERE t.user.id = :userId AND t.isChecked = false")
    long countByUserIdAndIsCheckedFalse(UUID userId);

    @Query("SELECT COALESCE(SUM(s.price * s.quantity), 0) FROM ShoppingItem s WHERE s.user.id = :userId AND s.isChecked = true AND s.budget IS NOT NULL AND s.budget.isDeleted = false")
    long sumUsedBudgetByUserId(@Param("userId") UUID userId);

    @Query("SELECT COALESCE(SUM(s.price * s.quantity), 0) FROM ShoppingItem s WHERE s.user.id = :userId AND s.isChecked = true AND s.budget IS NOT NULL AND s.budget.isDeleted = false AND s.budget.occasion.id = :occasionId")
    long sumUsedBudgetByUserIdAndOccasionId(@Param("userId") UUID userId, @Param("occasionId") UUID occasionId);

    @Query("SELECT COUNT(s) FROM ShoppingItem s WHERE s.budget.id = :budgetId")
    long countByBudgetId(@Param("budgetId") UUID budgetId);

    @Query("SELECT COUNT(s) FROM ShoppingItem s WHERE s.budget.id = :budgetId AND s.isChecked = false")
    long countByBudgetIdAndIsCheckedFalse(@Param("budgetId") UUID budgetId);
}
