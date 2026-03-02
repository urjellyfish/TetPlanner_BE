package com.wecamp.TetPlanner_BE.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingProgressResponse {
    private long totalShoppingItem;
    private long remainingShoppingItem;
    private int percentage;
}
