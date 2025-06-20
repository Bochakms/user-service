package io.github.Bochakms;

import io.github.Bochakms.entity.User;
import io.github.Bochakms.util.HibernateUtil;

import org.hibernate.Session;
import java.time.LocalDateTime;

public class TestUser {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            
            User user = new User();
            user.setName("Test User");
            user.setEmail("test@example.com");
            user.setAge(30);
            user.setCreatedAt(LocalDateTime.now());
            
            session.save(user);
            session.getTransaction().commit();
            
            System.out.println("User saved with ID: " + user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}