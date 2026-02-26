package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.TaskRequest;
import com.wecamp.TetPlanner_BE.dto.request.UpdateTaskStatusRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskListResponse;
import com.wecamp.TetPlanner_BE.dto.response.TaskResponse;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import com.wecamp.TetPlanner_BE.service.ITaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
    public final ITaskService taskService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<TaskListResponse>>> getTasks(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) LocalDate dueDate,
            Pageable pageable
    ) {

        try {
            List<TaskListResponse> tasks = taskService.getTasks(categoryId, priority, status, dueDate, pageable).getContent();
            return ResponseEntity.ok(new BaseResponse<>(true, "Tasks retrieved successfully", tasks));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(false, "Error retrieving tasks: " + e.getMessage(), null));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskResponse>> getTaskById(@PathVariable Long id) {
        try {
            TaskResponse task = taskService.getTask(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task retrieved successfully", task));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task not found with id: " + id, null));
        }
    }
    @PostMapping
    public ResponseEntity<BaseResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        try {
            TaskResponse task = taskService.createTask(taskRequest);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task created successfully", task));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteTask(@PathVariable Long id) {

        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task not found with id: " + id, null));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskResponse>> updateTask(@Valid @RequestBody TaskRequest taskRequest, @PathVariable Long id) {
        try {
            TaskResponse task = taskService.updateTask(taskRequest, id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task updated successfully", task));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task not found with id: " + id, null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskResponse>> updateTaskStatus(@Valid @RequestBody UpdateTaskStatusRequest status, @PathVariable Long id) {
        try {
            TaskResponse task = taskService.updateTaskStatus(status, id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task status updated successfully", task));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task not found with id: " + id, null));
        }
    }




}
