package com.wecamp.TetPlanner_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ShoppingItemsDTO {
    private String id;
    private String name;
    private Long price;
    private int quantity;
    private boolean isChecked;
    private Date createdAt;
}