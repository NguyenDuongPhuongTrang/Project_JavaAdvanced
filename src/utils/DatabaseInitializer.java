package utils;

import dao.UserDAO;
import dao.UserDAOImpl;
import enums.UserRole;
import models.accounts.User;

import java.sql.Connection;

public class DatabaseInitializer {
    private UserDAO userDAO = new UserDAOImpl();
    private Connection conn = DBConnection.getConnection();

    public void init() {
        if (userDAO.findByUsername("admin") == null) {
            String plainPass = "admin123";
            String hash = HashUtil.hashPassword(plainPass);
            User admin = new User("U001", "admin", hash, UserRole.ADMIN);
            userDAO.insert(conn, admin);
        }
    }
}