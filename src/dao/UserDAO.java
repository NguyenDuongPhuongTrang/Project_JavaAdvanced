package dao;

import enums.UserRole;
import models.accounts.User;

import java.sql.Connection;
import java.util.List;

public interface UserDAO {
    String getLastUserId();
    void insert(Connection conn, User user);
    User findByUsername(String username);
    void deleteById(Connection conn, String id);
    User findById(String id);
    List<User> getAllUsers();
}
