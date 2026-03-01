package com.wecamp.TetPlanner_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskProgressResponse {
    private long totalTasks;
    private long completedTasks;
    private int percentage;
}
