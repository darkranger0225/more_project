package com.pos.restaurantpos.service;

import com.pos.restaurantpos.entity.Dish;
import com.pos.restaurantpos.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public Dish findById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
    }

    public List<Dish> findAll() {
        return dishRepository.findAllByOrderBySortOrderAsc();
    }

    public List<Dish> findOnSale() {
        return dishRepository.findByStatusOrderBySortOrderAsc(Dish.Status.ON_SALE);
    }

    public List<Dish> findByCategory(Long categoryId) {
        return dishRepository.findByStatusAndCategoryIdOrderBySortOrderAsc(Dish.Status.ON_SALE, categoryId);
    }

    @Transactional
    public Dish create(Dish dish) {
        if (dish.getSortOrder() == null) {
            dish.setSortOrder(0);
        }
        if (dish.getStatus() == null) {
            dish.setStatus(Dish.Status.ON_SALE);
        }
        return dishRepository.save(dish);
    }

    @Transactional
    public Dish update(Long id, Dish dish) {
        Dish existing = findById(id);
        existing.setName(dish.getName());
        existing.setPrice(dish.getPrice());
        existing.setDescription(dish.getDescription());
        existing.setImage(dish.getImage());
        existing.setCategory(dish.getCategory());
        existing.setSortOrder(dish.getSortOrder());
        return dishRepository.save(existing);
    }

    @Transactional
    public void onSale(Long id) {
        Dish dish = findById(id);
        dish.setStatus(Dish.Status.ON_SALE);
        dishRepository.save(dish);
    }

    @Transactional
    public void offSale(Long id) {
        Dish dish = findById(id);
        dish.setStatus(Dish.Status.OFF_SALE);
        dishRepository.save(dish);
    }

    @Transactional
    public void delete(Long id) {
        dishRepository.deleteById(id);
    }

    public long countOnSale() {
        return dishRepository.countByStatus(Dish.Status.ON_SALE);
    }
}
