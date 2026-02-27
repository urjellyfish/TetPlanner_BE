package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.TaskCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskCategoryResponse;
import com.wecamp.TetPlanner_BE.service.ITaskCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-categories")
@AllArgsConstructor
public class TaskCategoryController {
    private final ITaskCategoryService taskCategoryService;
    @GetMapping
    public ResponseEntity<BaseResponse<List<TaskCategoryResponse>>> getAllTaskCategories() {
        List<TaskCategoryResponse> categories = taskCategoryService.getAll();
        return ResponseEntity.ok(
                new BaseResponse<>(true, "Task categories retrieved successfully", categories)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskCategoryResponse>> getTaskCategoryById(@PathVariable Long id){
        try {
            TaskCategoryResponse category = taskCategoryService.getById(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task category retrieved successfully", category));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Task category not found with id: " + id, null));
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<TaskCategoryResponse>> createTaskCategory(@Valid @RequestBody TaskCategoryRequest request) {
        try {
            TaskCategoryResponse createdCategory = taskCategoryService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(true, "Task category created successfully", createdCategory));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error creating task category: " + e.getMessage(), null));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskCategoryResponse>> updateTaskCategory( @PathVariable Long id, @Valid @RequestBody TaskCategoryRequest request) {
        try {
            TaskCategoryResponse updatedCategory = taskCategoryService.update(id, request);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task category updated successfully", updatedCategory));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error updating task category: " + e.getMessage(), null));
        }
    }
        @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> deleteTaskCategory(@PathVariable Long id) {
        try {
            taskCategoryService.delete(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task category deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, "Error deleting task category: " + e.getMessage(), null));
        }
    }

}
