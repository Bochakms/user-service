package io.github.Bochakms;

import io.github.Bochakms.dao.UserDao;
import io.github.Bochakms.dao.UserDaoImpl;
import io.github.Bochakms.entity.User;
import io.github.Bochakms.util.HibernateUtil;

import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
    	
    	try {
            boolean running = true;
            while (running) {
                printMenu();
                String input = scanner.nextLine();
                
                if (!input.matches("\\d+")) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }
                
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        getUserById();
                        break;
                    case 3:
                        getAllUsers();
                        break;
                    case 4:
                        updateUser();
                        break;
                    case 5:
                        deleteUser();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
            System.out.println("Application shutdown complete.");
        }
    }

    private static void printMenu() {
        System.out.println("\n--- User Management System ---");
        System.out.println("1. Create User");
        System.out.println("2. Get User by ID");
        System.out.println("3. Get All Users");
        System.out.println("4. Update User");
        System.out.println("5. Delete User");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createUser() {
        try {
            User user = new User();
            System.out.print("Enter name: ");
            user.setName(scanner.nextLine());
            
            System.out.print("Enter email: ");
            user.setEmail(scanner.nextLine());
            
            System.out.print("Enter age: ");
            user.setAge(Integer.parseInt(scanner.nextLine()));
            
            userDao.createUser(user);
            System.out.println("User created successfully!");
        } catch (Exception e) {
            System.err.println("Failed to create user: " + e.getMessage());
        }
    }

    private static void getUserById() {
        try {
            System.out.print("Enter user ID: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            Optional<User> user = userDao.getUserById(id);
            user.ifPresentOrElse(
                u -> System.out.println("User found: " + u),
                () -> System.out.println("User not found with ID: " + id)
            );
        } catch (Exception e) {
            System.err.println("Failed to get user: " + e.getMessage());
        }
    }

    private static void getAllUsers() {
        try {
            List<User> users = userDao.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found in database.");
            } else {
                System.out.println("Users list:");
                users.forEach(user -> System.out.println(" - " + user));
            }
        } catch (Exception e) {
            System.err.println("Failed to get users: " + e.getMessage());
        }
    }

    private static void updateUser() {
        try {
            System.out.print("Enter user ID to update: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            Optional<User> optionalUser = userDao.getUserById(id);
            if (optionalUser.isEmpty()) {
                System.out.println("User not found with ID: " + id);
                return;
            }
            
            User user = optionalUser.get();
            System.out.println("Current user data: " + user);
            
            System.out.print("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                user.setName(name);
            }
            
            System.out.print("Enter new email (leave blank to keep current): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                user.setEmail(email);
            }
            
            System.out.print("Enter new age (leave blank to keep current): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.isEmpty()) {
                user.setAge(Integer.parseInt(ageInput));
            }
            
            userDao.updateUser(user);
            System.out.println("User updated successfully!");
        } catch (Exception e) {
            System.err.println("Failed to update user: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        try {
            System.out.print("Enter user ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            
            Optional<User> user = userDao.getUserById(id);
            if (user.isPresent()) {
                userDao.deleteUser(id);
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Failed to delete user: " + e.getMessage());
        }
    }
}