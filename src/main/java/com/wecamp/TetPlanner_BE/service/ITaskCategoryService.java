package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.TaskCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskCategoryResponse;

import java.util.List;

public interface ITaskCategoryService {
    TaskCategoryResponse create(TaskCategoryRequest request);
    TaskCategoryResponse update(Long id, TaskCategoryRequest request);
    void delete(Long id);
    TaskCategoryResponse getById(Long id);
    List<TaskCategoryResponse> getAll();

}
