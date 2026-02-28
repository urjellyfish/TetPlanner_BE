package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.shoppingItem.CreateShoppingItemRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingItemDTO;
import com.wecamp.TetPlanner_BE.dto.request.shoppingItem.UpdateShoppingItemRequest;

import java.util.List;
import java.util.UUID;

public interface IShoppingItemService {
    ShoppingItemDTO create(UUID userId, CreateShoppingItemRequest request);

    ShoppingItemDTO update(UUID userId, UUID itemId, UpdateShoppingItemRequest request);

    void delete(UUID userId, UUID itemId);

    ShoppingItemDTO getById(UUID userId, UUID itemId);

    List<ShoppingItemDTO> getAllByUser(UUID userId);
}
