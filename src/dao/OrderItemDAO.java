package dao;

import models.orders.Order;
import models.orders.OrderItem;

import java.util.List;

public interface OrderItemDAO {
    void insert(OrderItem item);
    List<OrderItem> findByOrderId(String orderId);
}
