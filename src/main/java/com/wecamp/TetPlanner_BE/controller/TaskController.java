package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.task.TaskRequest;
import com.wecamp.TetPlanner_BE.dto.request.TaskUpdateRequest;
import com.wecamp.TetPlanner_BE.dto.request.task.UpdateTaskStatusRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskListResponse;
import com.wecamp.TetPlanner_BE.dto.response.TaskProgressResponse;
import com.wecamp.TetPlanner_BE.dto.response.TaskResponse;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import com.wecamp.TetPlanner_BE.service.ITaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    private final ITaskService taskService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<TaskListResponse>>> getTasks(
            Authentication authentication,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) LocalDate dueDate,
            Pageable pageable
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            List<TaskListResponse> tasks = taskService.getTasks(userId, categoryId, priority, status, dueDate, pageable).getContent();
            return ResponseEntity.ok(new BaseResponse<>(true, "Tasks retrieved successfully", tasks));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(false, "Something went wrong, please try again", null));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<BaseResponse<List<TaskListResponse>>> getTasksByUser(Authentication authentication) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            List<TaskListResponse> tasks = taskService.getTasksByUserId(userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Tasks retrieved successfully", tasks));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/progress")
    public ResponseEntity<BaseResponse<TaskProgressResponse>> getTaskProgress(Authentication authentication) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            TaskProgressResponse progress = taskService.getTaskProgress(userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task progress retrieved successfully", progress));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskResponse>> getTaskById(Authentication authentication, @PathVariable UUID id) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            TaskResponse task = taskService.getTask(id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task retrieved successfully", task));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task not found", null));
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest taskRequest,
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            TaskResponse task = taskService.createTask(taskRequest, userId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new BaseResponse<>(true, "Task created successfully", task));
        } catch (SecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskResponse>> updateTask(
            @Valid @RequestBody TaskUpdateRequest taskUpdateRequest,
            @PathVariable UUID id,
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            TaskResponse task = taskService.updateTask(taskUpdateRequest, id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task updated successfully", task));
        } catch (SecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new BaseResponse<>(false, "Task not found or you do not have permission to update this task", null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteTask(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            taskService.deleteTask(id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task deleted successfully", null));
        } catch (SecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new BaseResponse<>(false, "You do not have permission to delete this task", null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task not found", null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskResponse>> updateTaskStatus(
            Authentication authentication,
            @Valid @RequestBody UpdateTaskStatusRequest status,
            @PathVariable UUID id
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            TaskResponse task = taskService.updateTaskStatus(status, id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task status updated successfully", task));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task not found", null));
        }
    }
}
