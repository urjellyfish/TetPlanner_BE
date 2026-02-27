package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    Page<Budget> getByUserId(UUID userId, Pageable pageable);
    Optional<Budget> findByIdAndUserId(UUID id, UUID userId);
}
