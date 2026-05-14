package com.pos.restaurantpos.repository;

import com.pos.restaurantpos.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByStatusAndCategoryIdOrderBySortOrderAsc(Dish.Status status, Long categoryId);

    List<Dish> findByStatusOrderBySortOrderAsc(Dish.Status status);

    List<Dish> findByCategoryIdOrderBySortOrderAsc(Long categoryId);

    List<Dish> findAllByOrderBySortOrderAsc();

    long countByStatus(Dish.Status status);
}
