package io.github.Bochakms.dao;

import io.github.Bochakms.entity.User;
import io.github.Bochakms.util.HibernateUtil;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("hibernate.connection.url", postgresqlContainer.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgresqlContainer.getUsername());
        System.setProperty("hibernate.connection.password", postgresqlContainer.getPassword());
        
        userDao = new UserDaoImpl();
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.shutdown();
    }

    @BeforeEach
    void setUp() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    void createUser_ShouldPersistUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setAge(30);

        userDao.createUser(user);

        assertNotNull(user.getId());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@test.com");
        user.setAge(30);
        userDao.createUser(user);

        Optional<User> foundUser = userDao.getUserById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserNotExists() {
        Optional<User> foundUser = userDao.getUserById(999L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@test.com");
        user1.setAge(25);
        userDao.createUser(user1);

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@test.com");
        user2.setAge(30);
        userDao.createUser(user2);

        List<User> users = userDao.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void updateUser_ShouldUpdateExistingUser() {
        User user = new User();
        user.setName("Original Name");
        user.setEmail("original@test.com");
        user.setAge(25);
        userDao.createUser(user);

        user.setName("Updated Name");
        user.setEmail("updated@test.com");
        user.setAge(30);
        userDao.updateUser(user);

        Optional<User> updatedUser = userDao.getUserById(user.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("Updated Name", updatedUser.get().getName());
        assertEquals("updated@test.com", updatedUser.get().getEmail());
        assertEquals(30, updatedUser.get().getAge());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        User user = new User();
        user.setName("To Delete");
        user.setEmail("delete@test.com");
        user.setAge(40);
        userDao.createUser(user);

        userDao.deleteUser(user.getId());

        Optional<User> deletedUser = userDao.getUserById(user.getId());
        assertFalse(deletedUser.isPresent());
    }
}
