package com.wecamp.TetPlanner_BE.dto.request.budget;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateBudgetRequest {
    private String name;
    private UUID occasionId;
    private long totalAmount;
}
