package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.response.BudgetListResponse;
import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IBudgetService {
    BudgetListResponse getBudgetsForUser(UUID userId, Pageable pageable);
    BudgetSummaryResponse getBudgetSummaryForCurrentYear(UUID userId, UUID budgetId);
}
