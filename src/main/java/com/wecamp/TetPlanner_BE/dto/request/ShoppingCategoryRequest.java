package com.wecamp.TetPlanner_BE.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingCategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;
}
