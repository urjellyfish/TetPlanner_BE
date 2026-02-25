package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.Occasion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
@Repository
public interface OccasionRepository extends JpaRepository<Occasion, Long> {
   Optional<Occasion> findByDate(LocalDate date);
}
