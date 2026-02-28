package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.shoppingItem.CreateShoppingItemRequest;
import com.wecamp.TetPlanner_BE.dto.request.shoppingItem.UpdateShoppingItemRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingItemDTO;
import com.wecamp.TetPlanner_BE.service.IShoppingItemService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Data
@RequestMapping("/api/shopping-items")
@RequiredArgsConstructor
public class ShoppingItemController {

    private final IShoppingItemService shoppingItemService;

    @PostMapping
    public ResponseEntity<BaseResponse<ShoppingItemDTO>> createShoppingItem(
            Authentication authentication,
            @RequestBody CreateShoppingItemRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        ShoppingItemDTO response = shoppingItemService.create(userId, request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "Shopping item created successfully",
                        response
                )
        );
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<BaseResponse<ShoppingItemDTO>> updateShoppingItem(
            Authentication authentication,
            @PathVariable UUID itemId,
            @RequestBody UpdateShoppingItemRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        ShoppingItemDTO response = shoppingItemService.update(userId, itemId, request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "Shopping item updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<BaseResponse<Void>> deleteShoppingItem(
            Authentication authentication,
            @PathVariable UUID itemId
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        shoppingItemService.delete(userId, itemId);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "Shopping item deleted successfully",
                        null
                )
        );
    }
}