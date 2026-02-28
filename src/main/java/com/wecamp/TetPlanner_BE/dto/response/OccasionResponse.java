package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.Occasion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OccasionResponse {
    private UUID id;
    private String name;
    private LocalDate date;
    private List<OccasionTaskResponse> tasks;

    public static OccasionResponse convertToDTO(Occasion entity) {
        OccasionResponse dto = new OccasionResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        if (entity.getTasks() != null) {
            dto.setTasks(entity.getTasks().stream()
                    .filter(task -> !task.isDeleted())
                    .map(OccasionTaskResponse::convertToDTO)
                    .toList());
        } else {
            dto.setTasks(Collections.emptyList());
        }
        return dto;
    }
}
