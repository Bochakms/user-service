package io.github.Bochakms.dao;

import io.github.Bochakms.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Long create(User user);
    Optional<User> getById(Long id);
    List<User> getAll();
    void update(User user);
    void delete(Long id);
}