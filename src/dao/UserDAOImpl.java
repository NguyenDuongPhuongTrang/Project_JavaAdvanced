package dao;

import enums.UserRole;
import models.accounts.User;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    // lấy ra id cuối cùng
    @Override
    public String getLastUserIdByRole(String begin) {
        String sql = "SELECT userId FROM users WHERE userId LIKE ? ORDER BY userId DESC LIMIT 1";
        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, begin + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("userId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Dùng cho Register
    @Override
    public void insert(User user) {
        String sql = "INSERT INTO users(userId, username, password, role) VALUES (?, ?, ?, ?)";
        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Dùng cho login
    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("userId"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findById(String id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}
