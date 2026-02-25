package com.wecamp.TetPlanner_BE.dto.request;

import com.wecamp.TetPlanner_BE.entity.enums.Status;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {
    private Status status;
}
