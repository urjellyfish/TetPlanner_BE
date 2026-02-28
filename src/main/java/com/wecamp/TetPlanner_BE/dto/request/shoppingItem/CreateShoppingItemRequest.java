package com.wecamp.TetPlanner_BE.dto.request.shoppingItem;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateShoppingItemRequest {
    private String name;
    private Integer quantity;
    private Long price;
    private String note;

    private UUID categoryId;
    private UUID budgetId;
    private Long occasionId;
}
