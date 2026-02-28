package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.TaskCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskCategoryResponse;
import com.wecamp.TetPlanner_BE.security.JwtUtil;
import com.wecamp.TetPlanner_BE.service.ITaskCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-categories")
@AllArgsConstructor
public class TaskCategoryController {
    private final JwtUtil jwtUtil;
    private final ITaskCategoryService taskCategoryService;
    @GetMapping
    public ResponseEntity<BaseResponse<List<TaskCategoryResponse>>> getAllTaskCategories(@RequestHeader("Authorization") String token) {
        if(token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
        }
        String bearerToken = token.substring(7);
        UUID userID = jwtUtil.extractUserId(bearerToken);
        List<TaskCategoryResponse> categories = taskCategoryService.getAll(userID);
        return ResponseEntity.ok(
                new BaseResponse<>(true, "Task categories retrieved successfully", categories)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskCategoryResponse>> getTaskCategoryById(@RequestHeader("Authorization") String token, @PathVariable Long id){
        try {
            if(token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userID = jwtUtil.extractUserId(bearerToken);
            TaskCategoryResponse category = taskCategoryService.getById(id, userID);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task category retrieved successfully", category));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task category not found with id: " + id, null));
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<TaskCategoryResponse>> createTaskCategory(@RequestHeader("Authorization") String token,@Valid @RequestBody TaskCategoryRequest request) {
        try {
            if(token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userID = jwtUtil.extractUserId(bearerToken);
            TaskCategoryResponse createdCategory = taskCategoryService.create(request, userID);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(true, "Task category created successfully", createdCategory));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error creating task category: " + e.getMessage(), null));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskCategoryResponse>> updateTaskCategory(@RequestHeader("Authorization") String token, @PathVariable Long id, @Valid @RequestBody TaskCategoryRequest request) {
        try {
            if(token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userID = jwtUtil.extractUserId(bearerToken);
            TaskCategoryResponse updatedCategory = taskCategoryService.update(id, request, userID);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task category updated successfully", updatedCategory));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error updating task category: " + e.getMessage(), null));
        }
    }
        @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> deleteTaskCategory(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        try {
            if(token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new BaseResponse<>(false, "Unauthorized: Missing or invalid token", null));
            }
            String bearerToken = token.substring(7);
            UUID userID = jwtUtil.extractUserId(bearerToken);
            taskCategoryService.delete(id, userID);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task category deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error deleting task category: " + e.getMessage(), null));
        }
    }

}
