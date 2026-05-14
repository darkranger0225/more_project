package com.pos.restaurantpos.repository;

import com.pos.restaurantpos.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByStatusOrderBySortOrderAsc(Category.Status status);

    List<Category> findByStatusOrderBySortOrderDesc(Category.Status status);

    List<Category> findAllByOrderBySortOrderAsc();
}
