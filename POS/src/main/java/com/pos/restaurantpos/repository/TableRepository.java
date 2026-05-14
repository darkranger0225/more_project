package com.pos.restaurantpos.repository;

import com.pos.restaurantpos.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {

    Optional<Table> findByTableNo(String tableNo);

    boolean existsByTableNo(String tableNo);

    List<Table> findByStatusOrderBySortOrderAsc(Table.Status status);

    List<Table> findAllByOrderBySortOrderAsc();

    long countByStatus(Table.Status status);
}
