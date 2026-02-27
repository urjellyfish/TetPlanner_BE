package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.ShoppingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, UUID> {
    @Query("""
        SELECT COALESCE(SUM(s.price * s.quantity), 0)
        FROM ShoppingItem s
        WHERE s.budget.id = :budgetId
    """)
    Long getTotalExpenseByBudgetId(@Param("budgetId") UUID budgetId);

    List<ShoppingItem> findByBudgetId(UUID budgetId);

    List<ShoppingItem> findByBudget_IdIn(List<UUID> budgetIds);

    Page<ShoppingItem> getAllByBudgetId(UUID budgetId, Pageable pageable);
}
