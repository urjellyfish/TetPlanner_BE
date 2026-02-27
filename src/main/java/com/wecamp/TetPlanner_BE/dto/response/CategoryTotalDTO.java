package com.wecamp.TetPlanner_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryTotalDTO {
    private String categoryName;
    private Long total;
}