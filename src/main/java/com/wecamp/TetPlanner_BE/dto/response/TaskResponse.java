package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.Task;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private Long categoryId;
    private String categoryName;
    private UUID occasionId;
    private Priority priority;
    private Status status;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskResponse convertToDTO(Task entity) {
        TaskResponse dto = new TaskResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        if (entity.getCategory() != null) {
            dto.setCategoryName(entity.getCategory().getName());
            dto.setCategoryId(entity.getCategory().getId());
        }
        if (entity.getOccasion() != null) {
            dto.setOccasionId(entity.getOccasion().getId());
        }
        dto.setPriority(entity.getPriority());
        dto.setStatus(entity.getStatus());
        dto.setStartDate(entity.getStartDate());
        dto.setStartTime(entity.getStartTime());
        dto.setDueDate(entity.getDueDate());
        dto.setDueTime(entity.getDueTime());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
