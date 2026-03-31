package dao;

import enums.OrderStatus;
import models.orders.Order;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO{
    @Override
    public void insert(Order order) {
        String sql = "INSERT INTO orders (orderId, bookingId, customerId, totalAmount, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getOrderId());
            ps.setString(2, order.getBookingId());
            ps.setString(3, order.getCustomerId());
            ps.setDouble(4, order.getTotalAmount());
            ps.setString(5, order.getStatus().name());
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(order.getCreatedAt()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> findByBookingId(String bookingId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE bookingId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bookingId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order(
                        rs.getString("orderId"),
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getDouble("totalAmount"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Order o = new Order(
                        rs.getString("orderId"),
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getDouble("totalAmount"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void updateStatus(String orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE orderId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setString(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLastOrderId() {
        String sql = "SELECT orderId FROM orders ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString("orderId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Order findById(String orderId) {
        String sql = "SELECT * FROM orders WHERE orderId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getString("orderId"),
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getDouble("totalAmount"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        List<Order> list = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE customerId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                        rs.getString("orderId"),
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getDouble("totalAmount"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                list.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
