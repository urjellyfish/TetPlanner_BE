package com.wecamp.TetPlanner_BE.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OccasionUpdateRequest {
    @Size(min = 2, max = 200, message = "Name must be between 2 and 200 characters")
    private String name;

    private LocalDate date;
}
