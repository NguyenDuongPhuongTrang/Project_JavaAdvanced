package dao;

import enums.OrderStatus;
import models.orders.Order;
import models.orders.OrderItem;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO{

    @Override
    public void insert(OrderItem item) {
        String sql = "INSERT INTO order_items (orderItemsId, orderId, itemId, quantity, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getOrderItemsId());
            ps.setString(2, item.getOrderId());
            ps.setString(3, item.getItemId());
            ps.setInt(4, item.getQuantity());
            ps.setDouble(5, item.getPrice());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OrderItem> findByOrderId(String orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE orderId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem(
                        rs.getString("orderItemsId"),
                        rs.getString("orderId"),
                        rs.getString("itemId"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
