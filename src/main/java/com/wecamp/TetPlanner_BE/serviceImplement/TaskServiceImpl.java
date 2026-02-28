package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.request.TaskUpdateRequest;
import com.wecamp.TetPlanner_BE.dto.request.task.TaskRequest;
import com.wecamp.TetPlanner_BE.dto.request.task.UpdateTaskStatusRequest;
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
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class TaskServiceImpl implements ITaskService {
    private final TaskRepository taskRepository;
    private final TaskCategoryRepository taskCategoryRepository;
    private final OccasionRepository occasionRepository;
    private final UserRepository userRepository;

    @Override
    public Page<TaskListResponse> getTasks(UUID userId, Long categoryId, Priority priority, Status status, LocalDate dueDate, Pageable pageable) {
        return taskRepository.findAllWithFilters(userId, categoryId, priority, status, dueDate, pageable)
                .map(task -> {
                    Occasion occasion = task.getOccasion();
                    return TaskListResponse.convertToDTO(task, occasion);
                });
    }

    @Override
    public TaskResponse getTask(UUID id, UUID userId) {
        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to access this task");
        }
        return TaskResponse.convertToDTO(task);
    }

    @Override
    public TaskResponse createTask(TaskRequest taskRequest, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setUser(user);

        task.setPriority(taskRequest.getPriority() != null ? taskRequest.getPriority() : Priority.MEDIUM);
        task.setStatus(taskRequest.getStatus() != null ? taskRequest.getStatus() : Status.TODO);

        if (taskRequest.getCategoryId() != null) {
            TaskCategory category = taskCategoryRepository.findById(taskRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            task.setCategory(category);
        }

        if (taskRequest.getOccasionId() != null) {
            Occasion occasion = occasionRepository.findById(taskRequest.getOccasionId())
                    .orElseThrow(() -> new RuntimeException("Occasion not found"));
            if (!occasion.getUser().getId().equals(userId)) {
                throw new SecurityException("You do not have permission to use this occasion");
            }
            task.setOccasion(occasion);
        }

        LocalDate today = LocalDate.now();
        task.setStartDate(taskRequest.getStartDate() != null ? taskRequest.getStartDate() : today);
        task.setDueDate(taskRequest.getDueDate() != null ? taskRequest.getDueDate() : today);
        task.setStartTime(taskRequest.getStartTime() != null ? taskRequest.getStartTime() : LocalTime.of(0, 0));
        task.setDueTime(taskRequest.getDueTime() != null ? taskRequest.getDueTime() : LocalTime.of(23, 59));

        validateTimeLogic(task.getStartDate(), task.getStartTime(), task.getDueDate(), task.getDueTime());

        task.setCreatedAt(LocalDateTime.now());
        return TaskResponse.convertToDTO(taskRepository.save(task));
    }

    @Override
    public void deleteTask(UUID id, UUID userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to delete this task");
        }

        if (task.isDeleted()) {
            throw new RuntimeException("Task already deleted");
        }

        taskRepository.softDeleteById(id);
    }

    @Override
    public TaskResponse updateTask(TaskUpdateRequest request, UUID id, UUID userId) {
        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new SecurityException("Task not found or you do not have permission to update this task");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        if (request.isCategoryIdProvided()) {
            if (request.getCategoryId() != null) {
                TaskCategory category = taskCategoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                task.setCategory(category);
            } else {
                task.setCategory(null);
            }
        }

        if (request.isOccasionIdProvided()) {
            if (request.getOccasionId() != null) {
                Occasion occasion = occasionRepository.findById(request.getOccasionId())
                        .orElseThrow(() -> new RuntimeException("Occasion not found"));
                if (!occasion.getUser().getId().equals(userId)) {
                    throw new SecurityException("You do not have permission to use this occasion");
                }
                task.setOccasion(occasion);
            } else {
                task.setOccasion(null);
            }
        }

        if (request.getStartDate() != null) {
            task.setStartDate(request.getStartDate());
        }
        if (request.getStartTime() != null) {
            task.setStartTime(request.getStartTime());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getDueTime() != null) {
            task.setDueTime(request.getDueTime());
        }

        if (task.getStartDate() != null && task.getDueDate() != null) {
            validateTimeLogic(task.getStartDate(), task.getStartTime(), task.getDueDate(), task.getDueTime());
        }

        task.setUpdatedAt(LocalDateTime.now());
        return TaskResponse.convertToDTO(taskRepository.save(task));
    }

    @Override
    public TaskResponse updateTaskStatus(UpdateTaskStatusRequest request, UUID id, UUID userId) {
        Task task = taskRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to update this task");
        }

        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return TaskResponse.convertToDTO(task);
    }

    @Override
    public List<TaskListResponse> getTasksByUserId(UUID userId) {
        return taskRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(task -> TaskListResponse.convertToDTO(task, task.getOccasion()))
                .toList();
    }

    private void validateTimeLogic(LocalDate startDate, LocalTime startTime, LocalDate dueDate, LocalTime dueTime) {
        if (startDate.isAfter(dueDate)) {
            throw new RuntimeException("Start date cannot be after due date");
        }
        if (startDate.isEqual(dueDate) && startTime != null && dueTime != null) {
            if (startTime.isAfter(dueTime)) {
                throw new RuntimeException("Start time cannot be after due time on the same day");
            }
        }
    }
}
