package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;
import com.wecamp.TetPlanner_BE.service.IBudgetService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.UUID;

@RestController
@Data
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetController {
    private final IBudgetService budgetService;

    @GetMapping("/summary")
    public ResponseEntity<BaseResponse<BudgetSummaryResponse>> getBudgetSummary(
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        BudgetSummaryResponse response = budgetService.getBudgetSummaryForCurrentYear(userId);
        return ResponseEntity
                .ok(new BaseResponse<>(
                        true,
                        "Budget summary retrieved successfully",
                        response
                ));
    }
}
