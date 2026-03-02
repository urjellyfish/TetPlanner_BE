package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {
    @Modifying
    @Query("UPDATE TaskCategory t SET t.isDeleted = true WHERE t.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TaskCategory t WHERE t.name = :name AND (t.user.id = :userId OR t.user.id IS null )AND t.isDeleted = false")
    boolean existsByNameAndUserIdAndIsDeletedFalse(@Param("name") String name, @Param("userId") UUID userId);

    @Query("SELECT t FROM TaskCategory t WHERE (t.user.id = :userId OR t.user.id IS null) AND t.isDeleted = false")
    List<TaskCategory> findByUserIdAndIsDeletedFalse(@Param("userId") UUID userId);

    @Query("SELECT t FROM TaskCategory t WHERE t.id = :id AND (t.user.id = :userId OR t.user.id IS null)    AND t.isDeleted = false")
    Optional<TaskCategory> findByIdAndUserId(@Param("id") Long id, @Param("userId") UUID userId);
}
