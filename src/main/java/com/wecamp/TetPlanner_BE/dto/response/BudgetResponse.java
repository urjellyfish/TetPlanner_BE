package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.enums.BudgetStatus;
import lombok.Data;

@Data
public class BudgetResponse {
    private int totalBudget;
    private int totalExpense;
    private int totalExpectExpense;
    private int remainingBudget;
    private BudgetStatus status;
}
