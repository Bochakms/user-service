package io.github.Bochakms.service;

import io.github.Bochakms.entity.User;
import io.github.Bochakms.dao.UserDao;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
}