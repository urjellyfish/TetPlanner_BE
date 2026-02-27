package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;
import com.wecamp.TetPlanner_BE.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IBudgetService {
    Page<Budget> getBudgetsForUser(UUID userId, Pageable pageable);
    BudgetSummaryResponse getBudgetSummaryForCurrentYear(UUID userId, UUID budgetId);
}
