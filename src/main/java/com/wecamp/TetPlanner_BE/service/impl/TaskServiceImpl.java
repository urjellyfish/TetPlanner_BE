package com.wecamp.TetPlanner_BE.service.impl;

import com.wecamp.TetPlanner_BE.dto.request.TaskRequest;
import com.wecamp.TetPlanner_BE.dto.request.UpdateTaskStatusRequest;
import com.wecamp.TetPlanner_BE.dto.response.TaskListResponse;
import com.wecamp.TetPlanner_BE.dto.response.TaskResponse;
import com.wecamp.TetPlanner_BE.entity.Occasion;
import com.wecamp.TetPlanner_BE.entity.Task;
import com.wecamp.TetPlanner_BE.entity.TaskCategory;
import com.wecamp.TetPlanner_BE.entity.User;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import com.wecamp.TetPlanner_BE.repository.OccasionRepository;
import com.wecamp.TetPlanner_BE.repository.TaskCategoryRepository;
import com.wecamp.TetPlanner_BE.repository.TaskRepository;
import com.wecamp.TetPlanner_BE.repository.UserRepository;
import com.wecamp.TetPlanner_BE.service.ITaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
public class TaskServiceImpl implements ITaskService {
    private final TaskRepository taskRepository;
    private final TaskCategoryRepository taskCategoryRepository;
    private final OccasionRepository occasionRepository;
    private final UserRepository userRepository;


    @Override
    public Page<TaskListResponse> getTasks(Long categoryId, Priority priority, Status status, LocalDate dueDate, Pageable pageable) {
        return taskRepository.findAllWithFilters(categoryId, priority, status, dueDate, pageable)
                .map(task -> {
                    Occasion occasion = occasionRepository.findByDate(task.getDueDate())
                            .orElse(null);
                    return TaskListResponse.convertToDTO(task, occasion);
                });

    }

    @Override
    public TaskResponse getTask(Long id) {
            Task task = taskRepository.findById(id)
                    .filter(t -> !t.isDeleted())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
                return TaskResponse.convertToDTO(task);

    }

    @Override
    public TaskResponse createTask( TaskRequest taskRequest) {
        User user = userRepository.findById(taskRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

            Task task = new Task();
            task.setTitle(taskRequest.getTitle());
            task.setNote(taskRequest.getNote());
            TaskCategory category = taskCategoryRepository.findById(taskRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
            task.setCategory(category);
            task.setStatus(taskRequest.getStatus() != null ? taskRequest.getStatus() : Status.TODO);
            task.setDueDate(taskRequest.getDueDate());
            task.setUser(user);
            task.setDueTime(taskRequest.getDueTime());
            task.setPriority(taskRequest.getPriority());
            task.setEstimatedBudget(taskRequest.getEstimatedBudget());
            task.setCreatedAt(LocalDateTime.now());
        return TaskResponse.convertToDTO(taskRepository.save(task));
    }

    @Override

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id) .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.isDeleted()) {
            throw new RuntimeException("Task already deleted: " + id);
        }

        taskRepository.softDeleteById(id);

    }

    @Override
    public TaskResponse updateTask(TaskRequest request, Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(request.getTitle());
        task.setNote(request.getNote());
        TaskCategory category = taskCategoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        task.setCategory(category);
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());
        task.setDueTime(request.getDueTime());
        task.setPriority(request.getPriority());
        task.setUpdatedAt(LocalDateTime.now());
        task.setEstimatedBudget(request.getEstimatedBudget());


        return  TaskResponse.convertToDTO(taskRepository.save(task));
    }

    @Override
    public TaskResponse updateTaskStatus(UpdateTaskStatusRequest request, Long id) {
        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted()).orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return TaskResponse.convertToDTO(task);
    }
}
