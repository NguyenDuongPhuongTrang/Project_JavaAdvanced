package services;

import dao.CustomerDAO;
import dao.CustomerDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import enums.UserRole;
import exceptions.AuthenticationException;
import exceptions.DuplicateException;
import exceptions.ValidationException;
import models.accounts.Customer;
import models.accounts.User;
import utils.Config;
import utils.DBConnection;
import utils.HashUtil;
import validation.ValidatePassword;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAOImpl();
    private CustomerDAO customerDAO = new CustomerDAOImpl();

    // Đăng nhập
    public User login(String username, String password){
        User user = userDAO.findByUsername(username);
        if (user == null){
            throw new AuthenticationException(Config.RED + "\nUser không tồn tại" + Config.RESET);
        }
        boolean isValid = HashUtil.verifyPassword(password, user.getPassword());
        if (!isValid) {
            throw new AuthenticationException(Config.RED + "\nMật khẩu không chính xác" + Config.RESET);
        }
        return user;
    }

    // Đăng ký
    public void register(String username, String password, String name){
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            validateUsername(username);
            validatePassword(password);

            String userId = generateUserId();
            String hashPass = HashUtil.hashPassword(password);

            User user = new User(userId, username, hashPass, UserRole.CUSTOMER);
            userDAO.insert(conn, user);

            String[] words = name.trim().toLowerCase().split("\\s+");
            StringBuilder result = new StringBuilder();
            for (String word : words) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
            String fullName = result.toString().trim();

            String customerId = generateCustomerId();
            Customer customer = new Customer(customerId, userId, fullName, 0.0);
            customerDAO.insert(conn, customer);

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }

    // Các method validate
    public void validateUsername(String username) {
        if (userDAO.findByUsername(username) != null) {
            throw new DuplicateException(Config.RED + "Username đã tồn tại" + Config.RESET);
        }
    }

    public void validatePassword(String password) {
        if (!ValidatePassword.isValidatePassword(password)) {
            throw new ValidationException(Config.RED + "Mật khẩu phải có ít nhất 5 ký tự" + Config.RESET);
        }
    }

    // Tạo user ID
    public String generateUserId() {
        String lastId = userDAO.getLastUserId();

        if (lastId == null || lastId.isEmpty()) {
            return "U001";
        }

        int lastNumber = Integer.parseInt(lastId.substring(1));
        return "U" + String.format("%03d", lastNumber + 1);
    }

    // Tạo customer ID
    public String generateCustomerId() {
        String lastId = customerDAO.getLastCustomerId();

        if (lastId == null || lastId.isEmpty()) {
            return "CM001";
        }

        int lastNumber = Integer.parseInt(lastId.substring(2));
        return "CM" + String.format("%03d", lastNumber + 1);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}
