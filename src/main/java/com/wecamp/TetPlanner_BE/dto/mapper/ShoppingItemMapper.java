package com.wecamp.TetPlanner_BE.dto.mapper;

import com.wecamp.TetPlanner_BE.dto.response.ShoppingItemDTO;
import com.wecamp.TetPlanner_BE.entity.ShoppingItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShoppingItemMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "budget.id", target = "budgetId")
    @Mapping(source = "budget.name", target = "budgetName")
    @Mapping(source = "occasion.id", target = "occasionId")
    @Mapping(source = "occasion.name", target = "occasionName")
    // Use a Java expression for the calculated field, including a null safety check
    @Mapping(target = "totalAmount", expression = "java((item.getPrice() != null && item.getQuantity() != null) ? item.getPrice() * item.getQuantity() : 0L)")
    ShoppingItemDTO toDTO(ShoppingItem item);
}
