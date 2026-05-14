package com.pos.restaurantpos.repository;

import com.pos.restaurantpos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByRoleAndStatus(User.Role role, User.Status status);

    List<User> findByStatus(User.Status status);
}
