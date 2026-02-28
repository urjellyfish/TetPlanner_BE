package com.wecamp.TetPlanner_BE.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ShoppingItemDTO {
    private UUID id;

    private String name;
    private String note;

    private Long price;
    private Integer quantity;

    private Boolean isChecked;

    private UUID categoryId;
    private String categoryName;

    private UUID budgetId;
    private String budgetName;

    private long occasionId;
    private String occasionName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long totalAmount; // price * quantity
}
