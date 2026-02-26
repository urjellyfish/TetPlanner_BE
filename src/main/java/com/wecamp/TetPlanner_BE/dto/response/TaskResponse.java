package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.Task;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private Long categoryId;
    private String categoryName;
    private Priority priority;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private Status status;
    private BigDecimal estimatedBudget;
    private String note;

    public static TaskResponse convertToDTO(Task entity) {
        TaskResponse dto = new TaskResponse();
        dto.setId(entity.getId());
       dto.setTitle(entity.getTitle());
       if(entity.getCategory() != null) {
           dto.setCategoryName(entity.getCategory().getName());
           dto.setCategoryId(entity.getCategory().getId());
       }
        dto.setPriority(entity.getPriority());
        dto.setDueDate(entity.getDueDate());
        dto.setDueTime(entity.getDueTime());
        dto.setStatus(entity.getStatus());
        dto.setEstimatedBudget(entity.getEstimatedBudget());
        dto.setNote(entity.getNote());
        return dto;



    }
}
