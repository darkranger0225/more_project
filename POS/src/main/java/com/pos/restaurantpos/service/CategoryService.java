package com.pos.restaurantpos.service;

import com.pos.restaurantpos.entity.Category;
import com.pos.restaurantpos.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
    }

    public List<Category> findAll() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    public List<Category> findEnabled() {
        return categoryRepository.findByStatusOrderBySortOrderAsc(Category.Status.ENABLED);
    }

    @Transactional
    public Category create(Category category) {
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(Category.Status.ENABLED);
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, Category category) {
        Category existing = findById(id);
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setSortOrder(category.getSortOrder());
        return categoryRepository.save(existing);
    }

    @Transactional
    public void enable(Long id) {
        Category category = findById(id);
        category.setStatus(Category.Status.ENABLED);
        categoryRepository.save(category);
    }

    @Transactional
    public void disable(Long id) {
        Category category = findById(id);
        category.setStatus(Category.Status.DISABLED);
        categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
