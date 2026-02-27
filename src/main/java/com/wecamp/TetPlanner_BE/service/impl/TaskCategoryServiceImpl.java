package com.wecamp.TetPlanner_BE.service.impl;


import com.wecamp.TetPlanner_BE.dto.request.TaskCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskCategoryResponse;
import com.wecamp.TetPlanner_BE.entity.TaskCategory;
import com.wecamp.TetPlanner_BE.repository.TaskCategoryRepository;
import com.wecamp.TetPlanner_BE.service.ITaskCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TaskCategoryServiceImpl implements ITaskCategoryService {
    private final TaskCategoryRepository taskCategoryRepository;
    @Override
    public TaskCategoryResponse create(TaskCategoryRequest request) {
        TaskCategory dto = new TaskCategory();
        if (taskCategoryRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            throw new RuntimeException("Task category already exists");
        }
            dto.setName(request.getName());
            taskCategoryRepository.save(dto);

            return TaskCategoryResponse.convertToDTO(dto);
        }


    @Override
    public TaskCategoryResponse update(Long id, TaskCategoryRequest request) {


        TaskCategory dto = taskCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Task category not found"));
        if (taskCategoryRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            throw new RuntimeException("Task category already exists");
        }
        if(dto.isDeleted()){
            throw new RuntimeException("Task has arleady been deleted");
        }
        dto.setName(request.getName());
        taskCategoryRepository.save(dto);
        return TaskCategoryResponse.convertToDTO(dto) ;
    }

    @Override
    public void delete(Long id) {

        TaskCategory dto = taskCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Task category not found"));
        if(dto.isDeleted()){
            throw new RuntimeException("Task has already been deleted");
        }
        dto.setDeleted(true);
        taskCategoryRepository.save(dto);

    }

    @Override
    public TaskCategoryResponse getById(Long id) {
            TaskCategory dto = taskCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Task category not found"));
            if (dto.isDeleted()) {
                throw new RuntimeException("Task category not found");
            }
            return TaskCategoryResponse.convertToDTO(dto) ;

    }

    @Override
    public List<TaskCategoryResponse> getAll() {
        return taskCategoryRepository.findAll().stream()
                .filter(taskCategory -> !taskCategory.isDeleted())
                .map(TaskCategoryResponse::convertToDTO)
                .toList();
    }
}
