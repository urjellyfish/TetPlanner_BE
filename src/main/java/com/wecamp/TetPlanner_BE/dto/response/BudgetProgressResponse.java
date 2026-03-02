package com.wecamp.TetPlanner_BE.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BudgetProgressResponse {
    private long totalBudget;
    private long usedBudget;
    private int percentage;
}
