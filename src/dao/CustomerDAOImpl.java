package dao;

import models.accounts.Customer;
import models.accounts.Staff;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public void insert(Connection conn, Customer customer) {
        String sql = "INSERT INTO customers(customerId, userId, name, balance) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getUserId());
            ps.setString(3, customer.getName());
            ps.setDouble(4, customer.getBalance());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLastCustomerId() {
        String sql = "SELECT customerId FROM customers ORDER BY customerId DESC LIMIT 1";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            if (rs.next()) {
                return rs.getString("customerId");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void deleteByUserId(Connection conn, String userId) {
        String sql = "DELETE FROM customers WHERE userId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getString("customerId"),
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    @Override
    public Customer findById(String customerId) {
        String sql = "SELECT * FROM customers WHERE customerId = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, customerId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getString("customerId"),
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getDouble("balance")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customers SET name = ?, balance = ? WHERE customerId = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, customer.getName());
            ps.setDouble(2, customer.getBalance());
            ps.setString(3, customer.getCustomerId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer findByUserId(String userId) {
        String sql = "SELECT * FROM customers WHERE userId = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getString("customerId"),
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getDouble("balance")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Trong CustomerDAOImpl.java
    @Override
    public String getLastGuestCustomerId() {
        String sql = "SELECT customerId FROM customers WHERE customerId LIKE 'GUEST%' " +
                "ORDER BY customerId DESC LIMIT 1";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            if (rs.next()) {
                return rs.getString("customerId");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
