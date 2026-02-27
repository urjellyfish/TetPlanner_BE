package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.mapper.BudgetMapper;
import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;
import com.wecamp.TetPlanner_BE.entity.Budget;
import com.wecamp.TetPlanner_BE.entity.ShoppingItem;
import com.wecamp.TetPlanner_BE.entity.enums.BudgetStatus;
import com.wecamp.TetPlanner_BE.exception.NotFound;
import com.wecamp.TetPlanner_BE.repository.BudgetRepository;
import com.wecamp.TetPlanner_BE.repository.ShoppingItemRepository;
import com.wecamp.TetPlanner_BE.service.IBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetService implements IBudgetService {

    private final BudgetRepository budgetRepository;
    private final ShoppingItemRepository shoppingItemRepository;
    private final BudgetMapper budgetMapper;

    @Override
    public Page<Budget> getBudgetsForUser(UUID userId, Pageable pageable) {
        return null;
    }

    @Override
    public BudgetSummaryResponse getBudgetSummaryForCurrentYear(UUID userId, UUID budgetId) {

        Budget budget = budgetRepository
                .findByIdAndUserId(budgetId, userId)
                .orElseThrow(() -> new NotFound("Budget not found"));

        List<ShoppingItem> items =
                shoppingItemRepository.findByBudgetId(budget.getId());

        long actualSpent = items.stream()
                .filter(ShoppingItem::getIsChecked)
                .mapToLong(i -> i.getPrice() * i.getQuantity())
                .sum();

        long expectedSpent = items.stream()
                .mapToLong(i -> i.getPrice() * i.getQuantity())
                .sum();

        long totalBudget = budget.getTotalAmount();

        return BudgetSummaryResponse.builder()
                .id(budget.getId())
                .name(budget.getName())
                .totalAmount(totalBudget)
                .actualSpent(actualSpent)
                .expectedSpent(expectedSpent)
                .actualRemaining(totalBudget - actualSpent)
                .expectedRemaining(totalBudget - expectedSpent)
                .shoppingItems(
                        items.stream()
                                .map(budgetMapper::toShoppingItemDTO)
                                .toList()
                )
                .build();
    }

    private BudgetStatus calculateStatus(int percentage) {
        if (percentage >= 100) return BudgetStatus.CRITICAL;
        if (percentage >= 80) return BudgetStatus.WARNING;
        return BudgetStatus.SAFE;
    }

}
