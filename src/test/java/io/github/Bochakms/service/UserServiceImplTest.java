package io.github.Bochakms.service;

import io.github.Bochakms.dao.UserDao;
import io.github.Bochakms.entity.User;
import io.github.Bochakms.exceptions.UserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@test.com");
        testUser.setAge(30);
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return null;
        }).when(userDao).createUser(any(User.class));

        User createdUser = userService.createUser(testUser);

        assertNotNull(createdUser.getId());
        assertEquals(testUser.getName(), createdUser.getName());
        verify(userDao).createUser(testUser);
    }

    @Test
    void createUser_ShouldThrowException_WhenDaoFails() {
        doThrow(new RuntimeException("DB error"))
            .when(userDao)
            .createUser(any(User.class));

        assertThrows(UserServiceException.class, () -> userService.createUser(testUser));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userDao.getUserById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getName(), foundUser.get().getName());
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserNotExists() {
        when(userDao.getUserById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(1L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void getUserById_ShouldThrowException_WhenDaoFails() {
        when(userDao.getUserById(1L)).thenThrow(new RuntimeException("DB error"));

        assertThrows(UserServiceException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        when(userDao.getAllUsers()).thenReturn(List.of(testUser));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(testUser.getName(), users.get(0).getName());
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsers() {
        when(userDao.getAllUsers()).thenReturn(Collections.emptyList());

        List<User> users = userService.getAllUsers();

        assertTrue(users.isEmpty());
    }

    @Test
    void getAllUsers_ShouldThrowException_WhenDaoFails() {
        when(userDao.getAllUsers()).thenThrow(new RuntimeException("DB error"));

        assertThrows(UserServiceException.class, () -> userService.getAllUsers());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        doNothing().when(userDao).updateUser(any(User.class));

        User updatedUser = userService.updateUser(testUser);

        assertEquals(testUser.getName(), updatedUser.getName());
        verify(userDao).updateUser(testUser);
    }

    @Test
    void updateUser_ShouldThrowException_WhenDaoFails() {
        doThrow(new RuntimeException("DB error"))
            .when(userDao)
            .updateUser(any(User.class));

        assertThrows(UserServiceException.class, () -> userService.updateUser(testUser));
    }

    @Test
    void deleteUser_ShouldCallDaoDelete() {
        doNothing().when(userDao).deleteUser(1L);

        userService.deleteUser(1L);

        verify(userDao).deleteUser(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenDaoFails() {
        doThrow(new RuntimeException("DB error")).when(userDao).deleteUser(1L);

        assertThrows(UserServiceException.class, () -> userService.deleteUser(1L));
    }
}