package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.Task;
import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("""
    SELECT t FROM Task t
    WHERE t.isDeleted = false
      AND (:categoryId IS NULL OR t.category.id = :categoryId)
      AND (:priority IS NULL OR t.priority = :priority)
      AND (:status IS NULL OR t.status = :status)
      AND (:dueDate IS NULL OR t.dueDate = :dueDate)
""")
    Page<Task> findAllWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("priority") Priority priority,
            @Param("status") Status status,
            @Param("dueDate") LocalDate dueDate,
            Pageable pageable
    );

    List<Task> findByUserIdAndIsDeletedFalse(UUID userId);

    @Modifying
    @Query("UPDATE Task t SET t.isDeleted = true WHERE t.id = :id")
    void softDeleteById(@Param("id") UUID id);
}
