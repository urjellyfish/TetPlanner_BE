package com.wecamp.TetPlanner_BE.dto.mapper;

import com.wecamp.TetPlanner_BE.dto.response.*;
import com.wecamp.TetPlanner_BE.entity.ShoppingItem;
import com.wecamp.TetPlanner_BE.entity.enums.BudgetStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    //@Mapping(target = "shoppingItems", source = "shoppingItems")
    BudgetSummaryResponse toSummaryResponse(
            Long totalBudget,
            Long totalExpense,
            Long remainingBudget,
            int percentageUsed,
            BudgetStatus status,
            List<ShoppingItemsDTO> shoppingItems
    );

    ShoppingItemsDTO toShoppingItemDTO(ShoppingItem item);

    List<ShoppingItemsDTO> toShoppingItemDTOList(List<ShoppingItem> items);
}