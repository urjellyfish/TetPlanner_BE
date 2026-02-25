package com.wecamp.TetPlanner_BE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String email;

    private String hashPassword;

    private String fullName;

    private boolean isEmailVerified;

    private Instant createdAt;
}
