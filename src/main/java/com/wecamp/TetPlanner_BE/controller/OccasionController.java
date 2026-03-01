package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.OccasionRequest;
import com.wecamp.TetPlanner_BE.dto.request.OccasionUpdateRequest;
import com.wecamp.TetPlanner_BE.dto.response.OccasionResponse;
import com.wecamp.TetPlanner_BE.service.IOccasionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/occasions")
@AllArgsConstructor
public class OccasionController {
    private final IOccasionService occasionService;

    @PostMapping()
    public ResponseEntity<BaseResponse<OccasionResponse>> createOccasion(
            @Valid @RequestBody OccasionRequest request,
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            OccasionResponse occasion = occasionService.createOccasion(request, userId);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new BaseResponse<>(true, "Occasion created successfully", occasion));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OccasionResponse>> getOccasion(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            OccasionResponse occasion = occasionService.getOccasion(id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Occasion retrieved successfully", occasion));
        } catch (SecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Occasion not found", null));
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<OccasionResponse>>> getOccasionsByUser(
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            List<OccasionResponse> occasions = occasionService.getOccasionsByUserId(userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Occasions retrieved successfully", occasions));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/range")
    public ResponseEntity<BaseResponse<List<OccasionResponse>>> getOccasionsByDateRange(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            List<OccasionResponse> occasions = occasionService.getOccasionsByDateRange(userId, from, to);
            return ResponseEntity.ok(new BaseResponse<>(true, "Occasions retrieved successfully", occasions));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<OccasionResponse>> updateOccasion(
            @Valid @RequestBody OccasionUpdateRequest request,
            @PathVariable UUID id,
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            OccasionResponse occasion = occasionService.updateOccasion(request, id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Occasion updated successfully", occasion));
        } catch (SecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteOccasion(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        try {
            UUID userId = UUID.fromString(authentication.getName());
            occasionService.deleteOccasion(id, userId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Occasion has been deleted successfully", null));
        } catch (SecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new BaseResponse<>(false, "You do not have permission to delete this occasion", null));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(false, "Occasion not found", null));
        }
    }
}
