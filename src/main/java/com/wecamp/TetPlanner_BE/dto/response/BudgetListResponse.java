package com.wecamp.TetPlanner_BE.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BudgetListResponse {
    private SummaryDTO summary;
    private List<BudgetItemDTO> budgets;
}
