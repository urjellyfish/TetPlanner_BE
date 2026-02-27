package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;

import java.util.UUID;

public interface IBudgetService {
    BudgetSummaryResponse getBudgetSummaryForCurrentYear(UUID userId);
}
