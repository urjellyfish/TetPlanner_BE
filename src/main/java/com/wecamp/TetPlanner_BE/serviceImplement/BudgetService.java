package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.mapper.BudgetMapper;
import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;
import com.wecamp.TetPlanner_BE.dto.response.CategoryTotalDTO;
import com.wecamp.TetPlanner_BE.entity.Budget;
import com.wecamp.TetPlanner_BE.entity.enums.BudgetStatus;
import com.wecamp.TetPlanner_BE.exception.NotFound;
import com.wecamp.TetPlanner_BE.repository.BudgetRepository;
import com.wecamp.TetPlanner_BE.repository.ShoppingItemRepository;
import com.wecamp.TetPlanner_BE.service.IBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public BudgetSummaryResponse getBudgetSummaryForCurrentYear(UUID userId) {

        int currentYear = Year.now().getValue();

        Budget budget = budgetRepository
                .findCurrentYearBudget(userId, currentYear)
                .orElseThrow(() -> new NotFound("Budget not found"));

        Long totalExpense =
                shoppingItemRepository.getTotalExpenseByBudgetId(budget.getId());

        Long totalBudget = budget.getTotalAmount();

        int percentageUsed = totalBudget == 0
                ? 0
                : (int) ((totalExpense * 100) / totalBudget);

        BudgetStatus status = calculateStatus(percentageUsed);

        List<CategoryTotalDTO> breakdown =
                shoppingItemRepository.getBreakdownByBudgetId(budget.getId());

        return budgetMapper.toSummaryResponse(
                totalBudget,
                totalExpense,
                totalBudget - totalExpense,
                percentageUsed,
                status,
                breakdown
        );
    }

    private BudgetStatus calculateStatus(int percentage) {
        if (percentage >= 100) return BudgetStatus.CRITICAL;
        if (percentage >= 80) return BudgetStatus.WARNING;
        return BudgetStatus.SAFE;
    }

}
