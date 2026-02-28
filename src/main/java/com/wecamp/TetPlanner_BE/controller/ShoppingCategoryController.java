package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.ShoppingCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingCategoryResponse;
import com.wecamp.TetPlanner_BE.security.JwtUtil;
import com.wecamp.TetPlanner_BE.service.IShoppingCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shopping-categories")
@AllArgsConstructor
public class ShoppingCategoryController {
    private final JwtUtil jwtUtil;
    private final IShoppingCategoryService shoppingCategoryService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<ShoppingCategoryResponse>>> getAllShoppingCategories(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
        }
        String bearerToken = token.substring(7);
        UUID userId = jwtUtil.extractUserId(bearerToken);
        List<ShoppingCategoryResponse> categories = shoppingCategoryService.getAll(userId);
        return ResponseEntity.ok(
                new BaseResponse<>(true, "Shopping categories retrieved successfully", categories)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ShoppingCategoryResponse>> getShoppingCategoryById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userId = jwtUtil.extractUserId(bearerToken);
            ShoppingCategoryResponse category = shoppingCategoryService.getById(id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Shopping category retrieved successfully", category));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Shopping category not found with id: " + id, null));
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ShoppingCategoryResponse>> createShoppingCategory(@RequestHeader("Authorization") String token, @Valid @RequestBody ShoppingCategoryRequest request) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userId = jwtUtil.extractUserId(bearerToken);
            ShoppingCategoryResponse createdCategory = shoppingCategoryService.create(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(true, "Shopping category created successfully", createdCategory));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error creating shopping category: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ShoppingCategoryResponse>> updateShoppingCategory(@RequestHeader("Authorization") String token, @PathVariable Long id, @Valid @RequestBody ShoppingCategoryRequest request) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userId = jwtUtil.extractUserId(bearerToken);
            ShoppingCategoryResponse updatedCategory = shoppingCategoryService.update(id, request, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Shopping category updated successfully", updatedCategory));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error updating shopping category: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> deleteShoppingCategory(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userId = jwtUtil.extractUserId(bearerToken);
            shoppingCategoryService.delete(id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Shopping category deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error deleting shopping category: " + e.getMessage(), null));
        }
    }
}
