package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.TaskRequest;
import com.wecamp.TetPlanner_BE.dto.request.TaskUpdateRequest;
import com.wecamp.TetPlanner_BE.dto.request.UpdateTaskStatusRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskListResponse;
import com.wecamp.TetPlanner_BE.dto.response.TaskResponse;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import com.wecamp.TetPlanner_BE.security.JwtUtil;
import com.wecamp.TetPlanner_BE.service.ITaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    private final ITaskService taskService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<BaseResponse<List<TaskListResponse>>> getTasks(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) LocalDate dueDate,
            Pageable pageable
    ) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userId = jwtUtil.extractUserId(bearerToken);
            List<TaskListResponse> tasks = taskService.getTasks(userId, categoryId, priority, status, dueDate, pageable).getContent();
            return ResponseEntity.ok(new BaseResponse<>(true, "Tasks retrieved successfully", tasks));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(false, "Something went wrong, please try again", null));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<BaseResponse<List<TaskListResponse>>> getTasksByUser(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userId = jwtUtil.extractUserId(bearerToken);
            List<TaskListResponse> tasks = taskService.getTasksByUserId(userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Tasks retrieved successfully", tasks));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskResponse>> getTaskById(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userId = jwtUtil.extractUserId(bearerToken);
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
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Invalid or missing authorization token", null));
            }

            String token = authorizationHeader.substring(7);
            UUID userId = jwtUtil.extractUserId(token);
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
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Invalid or missing authorization token", null));
            }

            String token = authorizationHeader.substring(7);
            UUID userId = jwtUtil.extractUserId(token);
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
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Invalid or missing authorization token", null));
            }

            String token = authorizationHeader.substring(7);
            UUID userId = jwtUtil.extractUserId(token);
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
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateTaskStatusRequest status,
            @PathVariable UUID id
    ) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userId = jwtUtil.extractUserId(bearerToken);
            TaskResponse task = taskService.updateTaskStatus(status, id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task status updated successfully", task));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task not found", null));
        }
    }
}
