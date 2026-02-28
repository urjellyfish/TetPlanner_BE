package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.ShoppingCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingCategoryResponse;

import java.util.List;
import java.util.UUID;

public interface IShoppingCategoryService {
    ShoppingCategoryResponse create(ShoppingCategoryRequest request, UUID userId);
    ShoppingCategoryResponse update(Long id, ShoppingCategoryRequest request, UUID userId);
    void delete(Long id, UUID userId);
    ShoppingCategoryResponse getById(Long id, UUID userId);
    List<ShoppingCategoryResponse> getAll(UUID userId);
}
