package services;

import dao.*;
import enums.OrderStatus;
import exceptions.NotFoundException;
import models.accounts.Staff;
import models.orders.Order;
import models.orders.OrderItem;
import models.trans.Transaction;
import utils.Config;

import java.util.List;

public class StaffService {
    private OrderDAO orderDAO = new OrderDAOImpl();
    private OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
    private TransactionService transactionService = new TransactionService();

    // lấy id order
    public Order getOrderById(String orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            System.out.println(Config.RED + "\nOrderID không tồn tại!" + Config.RESET);
            return null;
        }
        return order;
    }

    // Lấy chi tiết order
    public List<OrderItem> getOrderItems(String orderId) {
        return orderItemDAO.findByOrderId(orderId);
    }

    // Update trạng thái
    public boolean updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            System.out.println(Config.RED + "\nKhông tìm thấy Order!" + Config.RESET);
            return false;
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            System.out.println(Config.RED + "\nKhông thể cập nhật đơn đã hoàn thành!" + Config.RESET);
            return false;
        }

        orderDAO.updateStatus(orderId, newStatus);
        return true;
    }

    // tính tổng tiền
    public double getTotalFromTransactions(String customerId) {
        List<Transaction> list = transactionService.getByCustomerId(customerId);

        double total = 0;
        for (Transaction t : list) {
            total += t.getAmount();
        }
        return total;
    }
}
