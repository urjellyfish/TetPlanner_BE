package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.TaskRequest;
import com.wecamp.TetPlanner_BE.dto.request.UpdateTaskStatusRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskListResponse;
import com.wecamp.TetPlanner_BE.dto.response.TaskResponse;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


public interface ITaskService {
    Page<TaskListResponse> getTasks(Long categoryId,
                                    Priority priority,
                                    Status status,
                                    LocalDate dueDate,
                                    Pageable pageable);
    TaskResponse getTask(@PathVariable Long id);
    TaskResponse createTask(TaskRequest taskRequest);
    void deleteTask(@PathVariable
                    Long id);
    TaskResponse updateTask(TaskRequest request, Long id);
    TaskResponse updateTaskStatus(UpdateTaskStatusRequest status, Long id);



}
