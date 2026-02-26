package com.wecamp.TetPlanner_BE.dto.response;

import com.wecamp.TetPlanner_BE.entity.Occasion;
import com.wecamp.TetPlanner_BE.entity.Task;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskListResponse {
    private Long id;
    private String title;
    private String categoryName;
    private String timelineLabel;
    private Priority priority;
    private Status status;

    public static TaskListResponse convertToDTO(Task entity, Occasion occasion) {
        TaskListResponse dto = new TaskListResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        if(entity.getCategory() != null) {
            dto.setCategoryName(entity.getCategory().getName());
        }
        dto.setPriority(entity.getPriority());
        if(occasion != null) {
            dto.setTimelineLabel(occasion.getName());
        }else if(entity.getDueDate() != null) {
            dto.setTimelineLabel(entity.getDueDate().toString());
        }

        dto.setStatus(entity.getStatus());

        return dto;



    }
    }

