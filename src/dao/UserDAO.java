package dao;

import models.accounts.User;

import java.util.List;

public interface UserDAO {
    String getLastUserIdByRole(String role);
    void insert(User user);
    User findByUsername(String username);
    User findById(String id);
    List<User> getAllUsers();
}
