package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.ShoppingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingCategoryRepository extends JpaRepository<ShoppingCategory, Long> {
    @Modifying
    @Query("UPDATE ShoppingCategory s SET s.isDeleted = true WHERE s.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ShoppingCategory s WHERE s.name = :name AND (s.user.id = :userId OR s.user.id IS null )AND s.isDeleted = false")
    boolean existsByNameAndUserIdAndIsDeletedFalse(@Param("name") String name, @Param("userId") UUID userId);

    @Query("SELECT s FROM ShoppingCategory s WHERE (s.user.id = :userId OR s.user.id IS null ) AND s.isDeleted = false")
    List<ShoppingCategory> findByUserIdAndIsDeletedFalse(@Param("userId") UUID userId);

    @Query("SELECT s FROM ShoppingCategory s WHERE s.id = :id AND (s.user.id = :userId OR s.user.id IS null )")
    Optional<ShoppingCategory> findByIdAndUserId(@Param("id") Long id, @Param("userId") UUID userId);
}
