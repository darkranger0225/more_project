package com.pos.restaurantpos.service;

import com.pos.restaurantpos.entity.Table;
import com.pos.restaurantpos.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;

    public Table findById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("桌台不存在"));
    }

    public Table findByTableNo(String tableNo) {
        return tableRepository.findByTableNo(tableNo)
                .orElseThrow(() -> new RuntimeException("桌台不存在"));
    }

    public List<Table> findAll() {
        return tableRepository.findAllByOrderBySortOrderAsc();
    }

    public List<Table> findFree() {
        return tableRepository.findByStatusOrderBySortOrderAsc(Table.Status.FREE);
    }

    public List<Table> findOccupied() {
        return tableRepository.findByStatusOrderBySortOrderAsc(Table.Status.OCCUPIED);
    }

    @Transactional
    public Table create(Table table) {
        if (tableRepository.existsByTableNo(table.getTableNo())) {
            throw new RuntimeException("桌台编号已存在");
        }
        if (table.getSortOrder() == null) {
            table.setSortOrder(0);
        }
        if (table.getStatus() == null) {
            table.setStatus(Table.Status.FREE);
        }
        return tableRepository.save(table);
    }

    @Transactional
    public Table update(Long id, Table table) {
        Table existing = findById(id);
        existing.setName(table.getName());
        existing.setCapacity(table.getCapacity());
        existing.setArea(table.getArea());
        existing.setSortOrder(table.getSortOrder());
        return tableRepository.save(existing);
    }

    @Transactional
    public void occupy(Long id) {
        Table table = findById(id);
        table.setStatus(Table.Status.OCCUPIED);
        tableRepository.save(table);
    }

    @Transactional
    public void free(Long id) {
        Table table = findById(id);
        table.setStatus(Table.Status.FREE);
        tableRepository.save(table);
    }

    @Transactional
    public void delete(Long id) {
        tableRepository.deleteById(id);
    }

    public long countFree() {
        return tableRepository.countByStatus(Table.Status.FREE);
    }

    public long countOccupied() {
        return tableRepository.countByStatus(Table.Status.OCCUPIED);
    }
}
