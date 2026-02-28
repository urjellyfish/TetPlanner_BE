package com.wecamp.TetPlanner_BE.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BudgetItemDTO {
    private UUID id;
    private String name;
    private long totalBudget;
    private long expectedSpent;
    private long actualSpent;
    private long variance;
}