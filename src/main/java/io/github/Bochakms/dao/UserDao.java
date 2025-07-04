package io.github.Bochakms.dao;

import io.github.Bochakms.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void createUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(Long id);
}