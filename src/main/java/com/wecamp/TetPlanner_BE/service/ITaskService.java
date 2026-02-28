package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.TaskRequest;
import com.wecamp.TetPlanner_BE.dto.request.TaskUpdateRequest;
import com.wecamp.TetPlanner_BE.dto.request.UpdateTaskStatusRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskListResponse;
import com.wecamp.TetPlanner_BE.dto.response.TaskResponse;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ITaskService {
    Page<TaskListResponse> getTasks(UUID userId,
                                    Long categoryId,
                                    Priority priority,
                                    Status status,
                                    LocalDate dueDate,
                                    Pageable pageable);

    TaskResponse getTask(UUID id, UUID userId);

    TaskResponse createTask(TaskRequest taskRequest, UUID userId);

    void deleteTask(UUID id, UUID userId);

    TaskResponse updateTask(TaskUpdateRequest request, UUID id, UUID userId);

    TaskResponse updateTaskStatus(UpdateTaskStatusRequest status, UUID id, UUID userId);

    List<TaskListResponse> getTasksByUserId(UUID userId);
}
