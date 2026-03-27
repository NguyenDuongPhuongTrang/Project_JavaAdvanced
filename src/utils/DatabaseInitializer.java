package utils;

import dao.UserDAO;
import dao.UserDAOImpl;
import enums.UserRole;
import models.accounts.User;

public class DatabaseInitializer {
    private UserDAO userDAO = new UserDAOImpl();

    public void init() {
        if (userDAO.findByUsername("admin") == null) {
            String plainPass = "admin123";
            String hash = HashUtil.hashPassword(plainPass);
            User admin = new User("AD001", "admin", hash, UserRole.ADMIN);
            userDAO.insert(admin);
        }
    }
}