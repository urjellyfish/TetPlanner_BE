package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.TaskCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskCategoryResponse;

import java.util.List;
import java.util.UUID;

public interface ITaskCategoryService {
    TaskCategoryResponse create(TaskCategoryRequest request, UUID userID);
    TaskCategoryResponse update(Long id, TaskCategoryRequest request, UUID userID);
    void delete(Long id, UUID userID);
    TaskCategoryResponse getById(Long id, UUID userID);
    List<TaskCategoryResponse> getAll( UUID userID);

}
