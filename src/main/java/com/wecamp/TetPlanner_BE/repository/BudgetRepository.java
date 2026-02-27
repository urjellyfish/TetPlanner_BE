package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    Optional<Budget> findByIdAndUserId(UUID id, UUID userId);
}
