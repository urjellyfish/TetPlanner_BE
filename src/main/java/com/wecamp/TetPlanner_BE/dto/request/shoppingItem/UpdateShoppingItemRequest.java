package com.wecamp.TetPlanner_BE.dto.request.shoppingItem;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateShoppingItemRequest {
    private String name;
    private Integer quantity;
    private Long price;
    private String note;
    private Boolean isChecked;

    private Long categoryId;
    private UUID budgetId;
    private UUID occasionId;
}
