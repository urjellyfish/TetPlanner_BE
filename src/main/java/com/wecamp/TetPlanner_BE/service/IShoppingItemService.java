package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.entity.ShoppingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IShoppingItemService {
    Page<ShoppingItem> getShoppingItemsByBudgetId(String budgetId, Pageable pageable);
}
