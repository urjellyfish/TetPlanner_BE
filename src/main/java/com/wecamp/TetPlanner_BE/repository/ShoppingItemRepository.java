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

}
