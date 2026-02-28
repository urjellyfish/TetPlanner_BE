package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.CreateBudgetRequest;
import com.wecamp.TetPlanner_BE.dto.request.UpdateBudgetRequest;
import com.wecamp.TetPlanner_BE.dto.response.BudgetListResponse;
import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IBudgetService {
    BudgetSummaryResponse createBudget(UUID userId, CreateBudgetRequest request);
    BudgetSummaryResponse updateBudget(UUID userId, UUID budgetId, UpdateBudgetRequest request);
    void deleteBudget(UUID userId, UUID budgetId);
    BudgetListResponse getBudgetsForUser(UUID userId, Pageable pageable);
    BudgetSummaryResponse getBudgetSummaryForCurrentYear(UUID userId, UUID budgetId);
}
