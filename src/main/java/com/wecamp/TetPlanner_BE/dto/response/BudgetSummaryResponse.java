package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.enums.BudgetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetSummaryResponse {
    private int totalBudget;
    private int totalExpense;
    private int remainingBudget;
    private int percentageUsed;
    private BudgetStatus status;
    private BreakDownCategoryResponse breakDownByCategory;
}
