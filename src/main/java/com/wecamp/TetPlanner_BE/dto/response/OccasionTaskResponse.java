package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.Task;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OccasionTaskResponse {
    private UUID id;
    private String title;
    private Status status;
    private LocalDate dueDate;
    private LocalTime dueTime;

    public static OccasionTaskResponse convertToDTO(Task entity) {
        OccasionTaskResponse dto = new OccasionTaskResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setStatus(entity.getStatus());
        dto.setDueDate(entity.getDueDate());
        dto.setDueTime(entity.getDueTime());
        return dto;
    }
}
