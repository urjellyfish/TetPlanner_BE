package com.wecamp.TetPlanner_BE.dto.request;

import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Category is required")
    private Long categoryId;
    @NotNull(message = "Priority is required")
    private Priority priority;
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;
    @NotNull(message = "Due time is required")
    private LocalTime dueTime;
    private Status status;
    @DecimalMin(value = "0.0", inclusive = true, message = "Estimated budget must be zero or positive")
    private BigDecimal estimatedBudget;
    private String note;
    @NotNull(message = "User ID is required")
    private UUID userId;


}
