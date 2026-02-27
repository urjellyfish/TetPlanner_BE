package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {
    @Modifying
    @Query("UPDATE TaskCategory t SET t.isDeleted = true WHERE t.id = :id")
    void softDeleteById(@Param("id")Long id);

    boolean existsByName(String name);
}
