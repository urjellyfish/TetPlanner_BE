package com.wecamp.TetPlanner_BE.repository;

import com.wecamp.TetPlanner_BE.entity.Occasion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OccasionRepository extends JpaRepository<Occasion, UUID> {
    List<Occasion> findByUserId(UUID userId);

    List<Occasion> findByUserIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);

    Optional<Occasion> findByUserIdAndNameAndDate(UUID userId, String name, LocalDate date);

    Optional<Occasion> findByDate(LocalDate date);
}
