package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.dto.response.BreakDownCategoryResponse;
import com.wecamp.TetPlanner_BE.dto.response.CategoryTotalDTO;
import com.wecamp.TetPlanner_BE.entity.ShoppingItem;
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


    @Query("""
    SELECT new com.wecamp.TetPlanner_BE.dto.response.CategoryTotalDTO(
        s.category.name,
        SUM(s.price * s.quantity)
    )
    FROM ShoppingItem s
    WHERE s.budget.id = :budgetId
    GROUP BY s.category.name
""")
    List<CategoryTotalDTO> getBreakdownByBudgetId(UUID budgetId);
}
