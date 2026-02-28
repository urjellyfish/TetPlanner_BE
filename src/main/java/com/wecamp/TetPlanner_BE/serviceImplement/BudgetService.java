package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.mapper.BudgetMapper;
import com.wecamp.TetPlanner_BE.dto.request.budget.CreateBudgetRequest;
import com.wecamp.TetPlanner_BE.dto.request.budget.UpdateBudgetRequest;
import com.wecamp.TetPlanner_BE.dto.response.BudgetItemDTO;
import com.wecamp.TetPlanner_BE.dto.response.BudgetListResponse;
import com.wecamp.TetPlanner_BE.dto.response.BudgetSummaryResponse;
import com.wecamp.TetPlanner_BE.dto.response.SummaryDTO;
import com.wecamp.TetPlanner_BE.entity.Budget;
import com.wecamp.TetPlanner_BE.entity.Occasion;
import com.wecamp.TetPlanner_BE.entity.ShoppingItem;
import com.wecamp.TetPlanner_BE.entity.User;
import com.wecamp.TetPlanner_BE.entity.enums.BudgetStatus;
import com.wecamp.TetPlanner_BE.exception.NotFound;
import com.wecamp.TetPlanner_BE.repository.BudgetRepository;
import com.wecamp.TetPlanner_BE.repository.ShoppingItemRepository;
import com.wecamp.TetPlanner_BE.service.IBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService implements IBudgetService {

    private final BudgetRepository budgetRepository;
    private final ShoppingItemRepository shoppingItemRepository;
    private final BudgetMapper budgetMapper;

    @Override
    public BudgetSummaryResponse createBudget(UUID userId, CreateBudgetRequest request) {

        Budget budget = new Budget();
        budget.setUser(
                User.builder().id(userId).build()
        );
        budget.setName(request.getName());
        budget.setTotalAmount(request.getTotalAmount());
        budget.setCreatedAt(LocalDateTime.now());
        budget.setUpdatedAt(LocalDateTime.now());

        if (request.getOccasionId() != null) {
            budget.setOccasion(
                    Occasion.builder()
                            .id(request.getOccasionId())
                            .build()
            );
        }

        budgetRepository.save(budget);

        return BudgetSummaryResponse.builder()
                .id(budget.getId())
                .name(budget.getName())
                .totalAmount(budget.getTotalAmount())
                .actualSpent(0)
                .expectedSpent(0)
                .actualRemaining(budget.getTotalAmount())
                .expectedRemaining(budget.getTotalAmount())
                .shoppingItems(List.of())
                .build();
    }

    @Override
    public BudgetSummaryResponse updateBudget(UUID userId, UUID budgetId, UpdateBudgetRequest request) {
        Budget budget = budgetRepository
                .findByIdAndUserIdAndIsDeletedFalse(budgetId, userId)
                .orElseThrow(() -> new NotFound("Budget not found"));

        if (request.getTotalAmount() != null) {
            budget.setTotalAmount(request.getTotalAmount());
        }

        budget.setUpdatedAt(LocalDateTime.now());

        budgetRepository.save(budget);

        return getBudgetSummaryForCurrentYear(userId, budget.getId());
    }

    @Override
    public void deleteBudget(UUID userId, UUID budgetId) {
        Budget budget = budgetRepository
                .findByIdAndUserIdAndIsDeletedFalse(budgetId, userId)
                .orElseThrow(() -> new NotFound("Budget not found"));

        budget.setDeleted(true);
        budget.setUpdatedAt(LocalDateTime.now());

        budgetRepository.save(budget);
    }

    @Override
    public BudgetListResponse getBudgetsForUser(UUID userId, Pageable pageable) {
        Page<Budget> budgetPage = budgetRepository.getByUserIdAndIsDeletedFalse(userId, pageable);

        if (budgetPage.isEmpty()){
            throw new NotFound("No budgets found for the user");
        }
        List<Budget> budgets = budgetPage.getContent();

        List<UUID> budgetIds = budgets.stream()
                .map(Budget::getId)
                .toList();

        // Lấy tất cả shopping items của các budget đó
        List<ShoppingItem> allItems =
                shoppingItemRepository.findByBudget_IdIn(budgetIds);

        // Map budgetId -> items
        Map<UUID, List<ShoppingItem>> itemsByBudget =
                allItems.stream()
                        .collect(Collectors.groupingBy(
                                item -> item.getBudget().getId()
                        ));

        long grandTotalBudget = 0;
        long grandTotalExpected = 0;
        long grandTotalActual = 0;

        List<BudgetItemDTO> budgetDTOs = budgets.stream().map(budget -> {

            List<ShoppingItem> items =
                    itemsByBudget.getOrDefault(budget.getId(), List.of());

            long expected = items.stream()
                    .mapToLong(i -> i.getPrice() * i.getQuantity())
                    .sum();

            long actual = items.stream()
                    .filter(ShoppingItem::getIsChecked)
                    .mapToLong(i -> i.getPrice() * i.getQuantity())
                    .sum();

            long total = budget.getTotalAmount();
            long variance = total - actual;

            return BudgetItemDTO.builder()
                    .id(budget.getId())
                    .name(budget.getName())
                    .totalBudget(total)
                    .expectedSpent(expected)
                    .actualSpent(actual)
                    .variance(variance)
                    .build();

        }).toList();

        SummaryDTO summary = SummaryDTO.builder()
                .grandTotalBudget(
                        budgetDTOs.stream()
                                .mapToLong(BudgetItemDTO::getTotalBudget)
                                .sum()
                )
                .grandTotalExpectedSpent(
                        budgetDTOs.stream()
                                .mapToLong(BudgetItemDTO::getExpectedSpent)
                                .sum()
                )
                .grandTotalActualSpent(
                        budgetDTOs.stream()
                                .mapToLong(BudgetItemDTO::getActualSpent)
                                .sum()
                )
                .grandTotalVariance(
                        budgetDTOs.stream()
                                .mapToLong(BudgetItemDTO::getVariance)
                                .sum()
                )
                .build();

        return BudgetListResponse.builder()
                .summary(summary)
                .budgets(budgetDTOs)
                .build();
    }

    @Override
    public BudgetSummaryResponse getBudgetSummaryForCurrentYear(UUID userId, UUID budgetId) {

        Budget budget = budgetRepository
                .findByIdAndUserIdAndIsDeletedFalse(budgetId, userId)
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
