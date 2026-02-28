package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.request.OccasionRequest;
import com.wecamp.TetPlanner_BE.dto.request.OccasionUpdateRequest;
import com.wecamp.TetPlanner_BE.dto.response.OccasionResponse;
import com.wecamp.TetPlanner_BE.entity.Occasion;
import com.wecamp.TetPlanner_BE.entity.User;
import com.wecamp.TetPlanner_BE.repository.OccasionRepository;
import com.wecamp.TetPlanner_BE.repository.UserRepository;
import com.wecamp.TetPlanner_BE.service.IOccasionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OccasionService implements IOccasionService {
    private final OccasionRepository occasionRepository;
    private final UserRepository userRepository;

    @Override
    public OccasionResponse createOccasion(OccasionRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Occasion> existing = occasionRepository.findByUserIdAndNameAndDate(
                userId, request.getName(), request.getDate());
        if (existing.isPresent()) {
            throw new RuntimeException("An occasion with the same name and date already exists");
        }

        Occasion occasion = new Occasion();
        occasion.setName(request.getName());
        occasion.setDate(request.getDate());
        occasion.setCreatedAt(LocalDateTime.now());
        occasion.setUser(user);

        Occasion saved = occasionRepository.save(occasion);
        return OccasionResponse.convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OccasionResponse getOccasion(UUID id, UUID userId) {
        Occasion occasion = occasionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Occasion not found"));
        if (!occasion.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to access this occasion");
        }
        return OccasionResponse.convertToDTO(occasion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OccasionResponse> getOccasionsByUserId(UUID userId) {
        return occasionRepository.findByUserId(userId)
                .stream()
                .map(OccasionResponse::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OccasionResponse> getOccasionsByDateRange(UUID userId, LocalDate from, LocalDate to) {
        return occasionRepository.findByUserIdAndDateBetween(userId, from, to)
                .stream()
                .map(OccasionResponse::convertToDTO)
                .toList();
    }

    @Override
    public OccasionResponse updateOccasion(OccasionUpdateRequest request, UUID id, UUID userId) {
        Occasion occasion = occasionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Occasion not found"));
        if (!occasion.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to update this occasion");
        }

        String newName = request.getName() != null ? request.getName() : occasion.getName();
        LocalDate newDate = request.getDate() != null ? request.getDate() : occasion.getDate();

        Optional<Occasion> duplicate = occasionRepository.findByUserIdAndNameAndDate(userId, newName, newDate);
        if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
            throw new RuntimeException("An occasion with the same name and date already exists");
        }

        if (request.getName() != null) {
            occasion.setName(request.getName());
        }
        if (request.getDate() != null) {
            occasion.setDate(request.getDate());
        }

        Occasion updated = occasionRepository.save(occasion);
        return OccasionResponse.convertToDTO(updated);
    }

    @Override
    public void deleteOccasion(UUID id, UUID userId) {
        Occasion occasion = occasionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Occasion not found"));

        if (!occasion.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to delete this occasion");
        }

        occasionRepository.delete(occasion);
    }
}
