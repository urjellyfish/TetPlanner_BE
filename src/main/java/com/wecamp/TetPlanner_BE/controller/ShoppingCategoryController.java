package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.ShoppingCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingCategoryResponse;
import com.wecamp.TetPlanner_BE.service.IShoppingCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shopping-categories")
@AllArgsConstructor
public class ShoppingCategoryController {
    private final IShoppingCategoryService shoppingCategoryService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<ShoppingCategoryResponse>>> getAllShoppingCategories(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<ShoppingCategoryResponse> categories = shoppingCategoryService.getAll(userId);
        return ResponseEntity.ok(
                new BaseResponse<>(true, "Shopping categories retrieved successfully", categories)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ShoppingCategoryResponse>> getShoppingCategoryById(Authentication authentication, @PathVariable Long id) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            ShoppingCategoryResponse category = shoppingCategoryService.getById(id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Shopping category retrieved successfully", category));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Shopping category not found with id: " + id, null));
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ShoppingCategoryResponse>> createShoppingCategory(Authentication authentication, @Valid @RequestBody ShoppingCategoryRequest request) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            ShoppingCategoryResponse createdCategory = shoppingCategoryService.create(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(true, "Shopping category created successfully", createdCategory));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error creating shopping category: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ShoppingCategoryResponse>> updateShoppingCategory(Authentication authentication, @PathVariable Long id, @Valid @RequestBody ShoppingCategoryRequest request) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            ShoppingCategoryResponse updatedCategory = shoppingCategoryService.update(id, request, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Shopping category updated successfully", updatedCategory));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error updating shopping category: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> deleteShoppingCategory(Authentication authentication, @PathVariable Long id) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            shoppingCategoryService.delete(id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Shopping category deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error deleting shopping category: " + e.getMessage(), null));
        }
    }
}
