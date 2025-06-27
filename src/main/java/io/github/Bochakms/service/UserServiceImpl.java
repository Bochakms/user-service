package io.github.Bochakms.service;

import io.github.Bochakms.dao.UserDao;
import io.github.Bochakms.entity.User;
import io.github.Bochakms.exceptions.UserServiceException;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User user) {
        try {
            userDao.createUser(user);
            return user;
        } catch (Exception e) {
            throw new UserServiceException("Failed to create user", e);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try {
            return userDao.getUserById(id);
        } catch (Exception e) {
            throw new UserServiceException("Failed to get user by ID: " + id, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userDao.getAllUsers();
        } catch (Exception e) {
            throw new UserServiceException("Failed to get all users", e);
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            userDao.updateUser(user);
            return user;
        } catch (Exception e) {
            throw new UserServiceException("Failed to update user: " + user.getId(), e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            userDao.deleteUser(id);
        } catch (Exception e) {
            throw new UserServiceException("Failed to delete user: " + id, e);
        }
    }
}