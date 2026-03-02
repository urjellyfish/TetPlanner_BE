package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.shoppingItem.CreateShoppingItemRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingItemDTO;
import com.wecamp.TetPlanner_BE.dto.request.shoppingItem.UpdateShoppingItemRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingProgressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IShoppingItemService {
    ShoppingItemDTO create(UUID userId, CreateShoppingItemRequest request);

    ShoppingItemDTO update(UUID userId, UUID itemId, UpdateShoppingItemRequest request);

    void delete(UUID userId, UUID itemId);

    ShoppingItemDTO getById(UUID userId, UUID itemId);

    Page<ShoppingItemDTO> getAllByUser(UUID userId, Pageable pageable);

    ShoppingProgressResponse getShoppingProgress(UUID userId, UUID budgetId);
}
