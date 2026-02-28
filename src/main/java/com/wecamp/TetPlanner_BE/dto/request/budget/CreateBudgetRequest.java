package com.wecamp.TetPlanner_BE.dto.request.budget;

import lombok.Data;

@Data
public class CreateBudgetRequest {
    private String name;
    private Long occasionId;
    private long totalAmount;
}
