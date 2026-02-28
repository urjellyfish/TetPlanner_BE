package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.enums.BudgetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetSummaryResponse {
    private UUID id;
    private String name;
    private long totalAmount;
    private long actualSpent;
    private long expectedSpent;
    private long actualRemaining;
    private long expectedRemaining;
    private List<ShoppingItemsDTO> shoppingItems;
}
