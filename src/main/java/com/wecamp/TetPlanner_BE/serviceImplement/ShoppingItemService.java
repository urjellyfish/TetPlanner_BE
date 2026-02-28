package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.mapper.ShoppingItemMapper;
import com.wecamp.TetPlanner_BE.dto.request.shoppingItem.CreateShoppingItemRequest;
import com.wecamp.TetPlanner_BE.dto.response.ShoppingItemDTO;
import com.wecamp.TetPlanner_BE.dto.request.shoppingItem.UpdateShoppingItemRequest;
import com.wecamp.TetPlanner_BE.entity.*;
import com.wecamp.TetPlanner_BE.exception.NotFound;
import com.wecamp.TetPlanner_BE.repository.ShoppingItemRepository;
import com.wecamp.TetPlanner_BE.service.IShoppingItemService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class ShoppingItemService implements IShoppingItemService {

    private final ShoppingItemRepository shoppingItemRepository;
    private final ShoppingItemMapper shoppingItemMapper;

    @Transactional
    @Override
    public ShoppingItemDTO create(UUID userId, CreateShoppingItemRequest request) {
        ShoppingItem item = new ShoppingItem();

        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setQuantity(request.getQuantity());
        item.setNote(request.getNote());

        item.setUser(User.builder().id(userId).build());

        if (request.getCategoryId() != null) {
            item.setCategory(
                    ShoppingCategory.builder()
                            .id(request.getCategoryId())
                            .build()
            );
        }

        if (request.getBudgetId() != null) {
            item.setBudget(
                    Budget.builder()
                            .id(request.getBudgetId())
                            .build()
            );
        }

        if (request.getOccasionId() != null) {
            item.setOccasion(
                    Occasion.builder()
                            .id(request.getOccasionId())
                            .build()
            );
        }

        shoppingItemRepository.save(item);

        return shoppingItemMapper.toDTO(item);
    }

    @Override
    public ShoppingItemDTO update(UUID userId, UUID itemId, UpdateShoppingItemRequest request) {

        ShoppingItem item = shoppingItemRepository
                .findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new NotFound("Shopping item not found"));

        if (request.getName() != null)
            item.setName(request.getName());

        if (request.getPrice() != null)
            item.setPrice(request.getPrice());

        if (request.getQuantity() != null)
            item.setQuantity(request.getQuantity());

        if (request.getNote() != null)
            item.setNote(request.getNote());

        if (request.getIsChecked() != null)
            item.setIsChecked(request.getIsChecked());

        if (request.getCategoryId() != null)
            item.setCategory(
                    ShoppingCategory.builder().id(request.getCategoryId()).build()
            );

        if (request.getBudgetId() != null)
            item.setBudget(
                    Budget.builder().id(request.getBudgetId()).build()
            );

        if (request.getOccasionId() != null)
            item.setOccasion(
                    Occasion.builder().id(request.getOccasionId()).build()
            );

        shoppingItemRepository.save(item);

        return shoppingItemMapper.toDTO(item);
    }


    @Override
    public void delete(UUID userId, UUID itemId) {
        ShoppingItem item = shoppingItemRepository
                .findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new NotFound("Shopping item not found"));

        shoppingItemRepository.delete(item);
    }

    @Override
    public ShoppingItemDTO getById(UUID userId, UUID itemId) {
        ShoppingItem item = shoppingItemRepository
                .findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new NotFound("Shopping item not found"));

        return shoppingItemMapper.toDTO(item);
    }

    @Override
    public Page<ShoppingItemDTO> getAllByUser(UUID userId, Pageable pageable) {
        Page<ShoppingItem> page =
                shoppingItemRepository.findByUserId(userId, pageable);

        if(page.isEmpty()) {
            throw new NotFound("No shopping items found for the user");
        }

        return page.map(shoppingItemMapper::toDTO);
    }
}
