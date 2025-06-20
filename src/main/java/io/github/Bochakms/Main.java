package io.github.Bochakms;


import io.github.Bochakms.dao.UserDao;
import io.github.Bochakms.dao.UserDaoImpl;
import io.github.Bochakms.entity.User;
import io.github.Bochakms.util.HibernateUtil;
import java.time.LocalDateTime;
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
                int choice = Integer.parseInt(scanner.nextLine());
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

        } finally {
            HibernateUtil.shutdown();
            scanner.close();
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
        User user = new User();
        System.out.print("Enter name: ");
        user.setName(scanner.nextLine());
        System.out.print("Enter email: ");
        user.setEmail(scanner.nextLine());
        System.out.print("Enter age: ");
        user.setAge(Integer.parseInt(scanner.nextLine()));
        Long id = userDao.create(user);
        System.out.println("User created with ID: " + id);
    }

    private static void getUserById() {
        System.out.print("Enter user ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<User> user = userDao.getById(id);
        user.ifPresentOrElse(
                u -> System.out.println("User found: " + u),
                () -> System.out.println("User not found")
        );
    }

    private static void getAllUsers() {
        List<User> users = userDao.getAll();
        if (users.isEmpty()) {
            System.out.println("No users found");
        } else {
            System.out.println("Users:");
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.print("Enter user ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<User> optionalUser = userDao.getById(id);
        if (optionalUser.isEmpty()) {
            System.out.println("User not found");
            return;
        }
        User user = optionalUser.get();
        System.out.print("Enter new name (" + user.getName() + "): ");
        user.setName(scanner.nextLine());
        System.out.print("Enter new email (" + user.getEmail() + "): ");
        user.setEmail(scanner.nextLine());
        System.out.print("Enter new age (" + user.getAge() + "): ");
        user.setAge(Integer.parseInt(scanner.nextLine()));
        userDao.update(user);
        System.out.println("User updated");
    }

    private static void deleteUser() {
        System.out.print("Enter user ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        userDao.delete(id);
        System.out.println("User deleted");
    }
}