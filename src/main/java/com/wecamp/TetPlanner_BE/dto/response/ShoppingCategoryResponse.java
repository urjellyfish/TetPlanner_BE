package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.ShoppingCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCategoryResponse {
    private Long id;
    private String name;

    public static ShoppingCategoryResponse convertToDTO(ShoppingCategory entity) {
        ShoppingCategoryResponse dto = new ShoppingCategoryResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
