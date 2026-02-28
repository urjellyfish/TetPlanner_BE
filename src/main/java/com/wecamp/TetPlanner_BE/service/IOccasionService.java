package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.OccasionRequest;
import com.wecamp.TetPlanner_BE.dto.request.OccasionUpdateRequest;
import com.wecamp.TetPlanner_BE.dto.response.OccasionResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IOccasionService {

    OccasionResponse createOccasion(OccasionRequest request, UUID userId);

    OccasionResponse getOccasion(UUID id);

    List<OccasionResponse> getOccasionsByUserId(UUID userId);

    List<OccasionResponse> getOccasionsByDateRange(UUID userId, LocalDate from, LocalDate to);

    OccasionResponse updateOccasion(OccasionUpdateRequest request, UUID id, UUID userId);

    void deleteOccasion(UUID id, UUID userId);
}
