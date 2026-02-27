package com.wecamp.TetPlanner_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BreakDownCategoryResponse {
    private List<CategoryTotalDTO> categories;
}
