package com.wecamp.TetPlanner_BE.entity;

import com.wecamp.TetPlanner_BE.entity.enums.Priority;
import com.wecamp.TetPlanner_BE.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Table(name = "tasks")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate dueDate;
    private LocalTime dueTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "occasion_id")
    private Occasion occasion;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private TaskCategory category;
}
