package dao;

import enums.OrderStatus;
import models.orders.Order;

import java.util.List;

public interface OrderDAO {
    void insert(Order order);
    List<Order> findByBookingId(String bookingId);
    List<Order> findAll();
    void updateStatus(String orderId, OrderStatus status);
    String getLastOrderId();
    Order findById(String orderId);
    List<Order> findByCustomerId(String customerId);

}
