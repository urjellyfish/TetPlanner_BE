package com.wecamp.TetPlanner_BE.dto.request;

import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskUpdateRequest {
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    private String title;

    @Size(max = 300, message = "Description must be at most 300 characters")
    private String description;

    private Priority priority;
    private Status status;
    private Long categoryId;
    private UUID occasionId;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate dueDate;
    private LocalTime dueTime;


    private boolean categoryIdProvided = false;
    private boolean occasionIdProvided = false;

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        this.categoryIdProvided = true;
    }

    public void setOccasionId(UUID occasionId) {
        this.occasionId = occasionId;
        this.occasionIdProvided = true;
    }
}
