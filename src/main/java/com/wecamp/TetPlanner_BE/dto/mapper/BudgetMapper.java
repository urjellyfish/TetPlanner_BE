package com.wecamp.TetPlanner_BE.dto.mapper;

import com.wecamp.TetPlanner_BE.dto.response.*;
import com.wecamp.TetPlanner_BE.entity.enums.BudgetStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(target = "breakDownByCategory", source = "categories")
    BudgetSummaryResponse toSummaryResponse(
            Long totalBudget,
            Long totalExpense,
            Long remainingBudget,
            int percentageUsed,
            BudgetStatus status,
            List<CategoryTotalDTO> categories
    );

    default BreakDownCategoryResponse categoriesToBreakDownCategoryResponse(
            List<CategoryTotalDTO> categories) {
        return new BreakDownCategoryResponse(categories);
    }
}