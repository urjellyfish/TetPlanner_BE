package com.wecamp.TetPlanner_BE.service.impl;

import com.wecamp.TetPlanner_BE.dto.request.TaskCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskCategoryResponse;
import com.wecamp.TetPlanner_BE.entity.TaskCategory;
import com.wecamp.TetPlanner_BE.entity.User;
import com.wecamp.TetPlanner_BE.repository.TaskCategoryRepository;
import com.wecamp.TetPlanner_BE.repository.UserRepository;
import com.wecamp.TetPlanner_BE.service.ITaskCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class TaskCategoryServiceImpl implements ITaskCategoryService {
    private final TaskCategoryRepository taskCategoryRepository;
    private final UserRepository userRepository;

    @Override
    public TaskCategoryResponse create(TaskCategoryRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (taskCategoryRepository.existsByNameAndUserIdAndIsDeletedFalse(request.getName(), userId)) {
            throw new RuntimeException("Task category already exists");
        }

        TaskCategory category = new TaskCategory();
        category.setName(request.getName());
        category.setUser(user);
        taskCategoryRepository.save(category);

        return TaskCategoryResponse.convertToDTO(category);
    }

    @Override
    public TaskCategoryResponse update(Long id, TaskCategoryRequest request, UUID userId) {
        TaskCategory category = taskCategoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Task category not found or you do not have permission"));

        if (category.isDeleted()) {
            throw new RuntimeException("Task category has already been deleted");
        }

        if (!category.getName().equals(request.getName())
                && taskCategoryRepository.existsByNameAndUserIdAndIsDeletedFalse(request.getName(), userId)) {
            throw new RuntimeException("Task category with this name already exists");
        }

        category.setName(request.getName());
        taskCategoryRepository.save(category);
        return TaskCategoryResponse.convertToDTO(category);
    }

    @Override
    public void delete(Long id, UUID userId) {
        TaskCategory category = taskCategoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Task category not found or you do not have permission"));

        if (category.isDeleted()) {
            throw new RuntimeException("Task category has already been deleted");
        }

        taskCategoryRepository.softDeleteById(id);
    }

    @Override
    public TaskCategoryResponse getById(Long id, UUID userId) {
        TaskCategory category = taskCategoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Task category not found or you do not have permission"));

        if (category.isDeleted()) {
            throw new RuntimeException("Task category not found");
        }

        return TaskCategoryResponse.convertToDTO(category);
    }

    @Override
    public List<TaskCategoryResponse> getAll(UUID userId) {
        return taskCategoryRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(TaskCategoryResponse::convertToDTO)
                .toList();
    }
}
