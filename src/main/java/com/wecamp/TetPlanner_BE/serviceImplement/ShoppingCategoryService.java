package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.request.ShoppingCategoryRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingCategoryResponse;
import com.wecamp.TetPlanner_BE.entity.ShoppingCategory;
import com.wecamp.TetPlanner_BE.entity.User;
import com.wecamp.TetPlanner_BE.repository.ShoppingCategoryRepository;
import com.wecamp.TetPlanner_BE.repository.UserRepository;
import com.wecamp.TetPlanner_BE.service.IShoppingCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class ShoppingCategoryService implements IShoppingCategoryService {
    private final ShoppingCategoryRepository shoppingCategoryRepository;
    private final UserRepository userRepository;

    @Override
    public ShoppingCategoryResponse create(ShoppingCategoryRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (shoppingCategoryRepository.existsByNameAndUserIdAndIsDeletedFalse(request.getName(), userId)) {
            throw new RuntimeException("Shopping category already exists");
        }

        ShoppingCategory category = new ShoppingCategory();
        category.setName(request.getName());
        category.setUser(user);
        shoppingCategoryRepository.save(category);

        return ShoppingCategoryResponse.convertToDTO(category);
    }

    @Override
    public ShoppingCategoryResponse update(Long id, ShoppingCategoryRequest request, UUID userId) {
        ShoppingCategory category = shoppingCategoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Shopping category not found or you do not have permission"));

        if (category.isDeleted()) {
            throw new RuntimeException("Shopping category has already been deleted");
        }

        if (!category.getName().equals(request.getName())
                && shoppingCategoryRepository.existsByNameAndUserIdAndIsDeletedFalse(request.getName(), userId)) {
            throw new RuntimeException("Shopping category with this name already exists");
        }

        category.setName(request.getName());
        shoppingCategoryRepository.save(category);
        return ShoppingCategoryResponse.convertToDTO(category);
    }

    @Override
    public void delete(Long id, UUID userId) {
        ShoppingCategory category = shoppingCategoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Shopping category not found or you do not have permission"));

        if (category.isDeleted()) {
            throw new RuntimeException("Shopping category has already been deleted");
        }

        shoppingCategoryRepository.softDeleteById(id);
    }

    @Override
    public ShoppingCategoryResponse getById(Long id, UUID userId) {
        ShoppingCategory category = shoppingCategoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Shopping category not found or you do not have permission"));

        if (category.isDeleted()) {
            throw new RuntimeException("Shopping category not found");
        }

        return ShoppingCategoryResponse.convertToDTO(category);
    }

    @Override
    public List<ShoppingCategoryResponse> getAll(UUID userId) {
        return shoppingCategoryRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(ShoppingCategoryResponse::convertToDTO)
                .toList();
    }
}
