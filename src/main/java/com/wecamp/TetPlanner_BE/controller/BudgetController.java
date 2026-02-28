package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.budget.CreateBudgetRequest;
import com.wecamp.TetPlanner_BE.dto.request.budget.UpdateBudgetRequest;
import com.wecamp.TetPlanner_BE.dto.response.BudgetListResponse;
import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;
import com.wecamp.TetPlanner_BE.service.IBudgetService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Data
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetController {
    private final IBudgetService budgetService;

    @GetMapping
    public ResponseEntity<BaseResponse<BudgetListResponse>> getAllBudgets(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        BudgetListResponse response = budgetService.getBudgetsForUser(userId, Pageable.ofSize(size).withPage(page));
        return ResponseEntity
                .ok(new BaseResponse<>(
                        true,
                        "Budgets retrieved successfully",
                        response
                ));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<BudgetSummaryResponse>> createBudget(
            Authentication authentication,
            @RequestBody CreateBudgetRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        BudgetSummaryResponse response = budgetService.createBudget(userId, request);
        return ResponseEntity
                .ok(new BaseResponse<>(
                        true,
                        "Budget created successfully",
                        response
                ));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BaseResponse<BudgetSummaryResponse>> updateBudget(
            Authentication authentication,
            @PathVariable UUID budgetId,
            @RequestBody UpdateBudgetRequest request
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        BudgetSummaryResponse response = budgetService.updateBudget(userId, budgetId, request);
        return ResponseEntity
                .ok(new BaseResponse<>(
                        true,
                        "Budget updated successfully",
                        response
                ));
    }
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<BaseResponse<BudgetSummaryResponse>> deleteBudget(
            Authentication authentication,
            @PathVariable UUID budgetId
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        budgetService.deleteBudget(userId, budgetId);
        return ResponseEntity
                .ok(new BaseResponse<>(
                        true,
                        "Budget updated successfully",
                        null
                ));
    }

    @GetMapping("/{budgetId}/summary")
    public ResponseEntity<BaseResponse<BudgetSummaryResponse>> getBudgetSummary(
            Authentication authentication,
            @PathVariable UUID budgetId
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        BudgetSummaryResponse response = budgetService.getBudgetSummaryForCurrentYear(userId, budgetId);
        return ResponseEntity
                .ok(new BaseResponse<>(
                        true,
                        "Budget summary retrieved successfully",
                        response
                ));
    }
}
