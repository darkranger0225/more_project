package com.pos.restaurantpos.service;

import com.pos.restaurantpos.entity.User;
import com.pos.restaurantpos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findCashiers() {
        return userRepository.findByRoleAndStatus(User.Role.CASHIER, User.Status.ENABLED);
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(User.Status.ENABLED);
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = findById(id);
        existingUser.setRealName(user.getRealName());
        existingUser.setPhone(user.getPhone());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(existingUser);
    }

    @Transactional
    public void disableUser(Long id) {
        User user = findById(id);
        user.setStatus(User.Status.DISABLED);
        userRepository.save(user);
    }

    @Transactional
    public void enableUser(Long id) {
        User user = findById(id);
        user.setStatus(User.Status.ENABLED);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
