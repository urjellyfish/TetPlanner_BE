package com.wecamp.TetPlanner_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shopping_categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShoppingCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "uuid")
    private User user;
}
