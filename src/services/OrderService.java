package services;

import dao.*;
import enums.PaymentMethod;
import enums.TransType;
import enums.OrderStatus;
import models.accounts.Customer;
import models.orders.Order;
import models.orders.OrderItem;
import models.products.MenuItems;
import models.trans.Booking;
import models.trans.Transaction;
import observer.TransactionSubject;
import utils.Config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class OrderService {

    private OrderDAO orderDAO = new OrderDAOImpl();
    private OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
    private ItemDAO itemDAO = new ItemDAOImpl();
    private CustomerDAO customerDAO = new CustomerDAOImpl();

    private TransactionService transactionService = new TransactionService();

    // Xem danh sách order
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    // Hiển thị menu
    public List<MenuItems> showMenu() {
        return itemDAO.findAll();
    }

    // Tính tổng tiền đơn
    public double calculateTotal(Map<MenuItems, Integer> items) {
        double total = 0;
        for (Map.Entry<MenuItems, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    // Generate Order ID
    public String generateOrderId() {
        String lastId = orderDAO.getLastOrderId();
        if (lastId == null || lastId.isEmpty()) return "OD001";

        int lastNumber = Integer.parseInt(lastId.substring(2));
        return "OD" + String.format("%03d", lastNumber + 1);
    }

    // Generate OrderItem ID
    public String generateOrderItemId(String orderId, int index) {
        String orderNumber = orderId.substring(2);
        return "OI" + orderNumber + String.format("%02d", index);
    }

    // Đặt món
    public boolean placeOrder(String computerId, Map<MenuItems, Integer> items) {
        Booking booking = new BookingDAOImpl().findActiveByComputerId(computerId);

        if (booking == null) {
            System.out.println(Config.RED + "Không có phiên chơi đang hoạt động!" + Config.RESET);
            return false;
        }

        String customerId = booking.getCustomerId();
        Customer customer = customerDAO.findById(customerId);

        if (customer == null) {
            System.out.println(Config.RED + "Khách hàng không tồn tại!" + Config.RESET);
            return false;
        }

        double total = calculateTotal(items);

        PaymentMethod method;

        if (customer.getBalance() >= total) {
            method = PaymentMethod.BALANCE;
        } else {
            method = PaymentMethod.CASH;
        }

        String orderId = generateOrderId();

        // SỬA: Truyền thêm customerId
        Order order = new Order(orderId, booking.getBookingId(), customerId,
                total, OrderStatus.CONFIRMED, LocalDateTime.now());

        orderDAO.insert(order);

        // OrderItem
        int index = 1;
        for (Map.Entry<MenuItems, Integer> entry : items.entrySet()) {
            String orderItemId = generateOrderItemId(orderId, index++);
            OrderItem orderItem = new OrderItem(
                    orderItemId,
                    orderId,
                    entry.getKey().getItemId(),
                    entry.getValue(),
                    entry.getKey().getPrice()
            );
            orderItemDAO.insert(orderItem);
        }

        // Transaction
        Transaction tx = new Transaction(
                transactionService.generateTransactionId(),
                customerId,
                total,
                TransType.ORDER,
                method,
                "Thanh toán đơn F&B " + orderId,
                LocalDateTime.now()
        );

        TransactionSubject.getInstance().notifyObservers(tx);

        System.out.println(Config.GREEN_BRIGHT + "\nĐặt món thành công!" + Config.RESET);
        return true;
    }

    public boolean createManualOrder(String customerId, String bookingId,
                                     String computerId, Map<MenuItems, Integer> items) {

        if (items == null || items.isEmpty()) return false;

        double total = calculateTotal(items);
        Customer customer = null;

        if (customerId != null && !customerId.startsWith("GUEST")) {
            customer = customerDAO.findById(customerId);

            if (customer == null) {
                System.out.println(Config.RED + "Khách hàng không tồn tại!" + Config.RESET);
                return false;
            }
        }

        String orderId = generateOrderId();

        Order order = new Order(orderId, bookingId, customerId, total,
                OrderStatus.CONFIRMED, LocalDateTime.now());

        orderDAO.insert(order);

        // Tạo OrderItem
        int index = 1;
        for (Map.Entry<MenuItems, Integer> entry : items.entrySet()) {
            String orderItemId = generateOrderItemId(orderId, index++);
            OrderItem orderItem = new OrderItem(
                    orderItemId,
                    orderId,
                    entry.getKey().getItemId(),
                    entry.getValue(),
                    entry.getKey().getPrice()
            );
            orderItemDAO.insert(orderItem);
        }

        PaymentMethod method = PaymentMethod.CASH;

        if (customer != null && customer.getBalance() >= total) {
            method = PaymentMethod.BALANCE;
        }

        // Transaction
        if (customerId != null && !customerId.startsWith("GUEST")) {
            Transaction tx = new Transaction(
                    transactionService.generateTransactionId(),
                    customerId,
                    total,
                    TransType.ORDER,
                    method,
                    "Thanh toán đơn F&B (Manual) " + orderId,
                    LocalDateTime.now()
            );
            TransactionSubject.getInstance().notifyObservers(tx);
        } else {
            System.out.println(Config.YELLOW + "Đơn lẻ - Thu tiền mặt." + Config.RESET);
        }

        System.out.println(Config.GREEN_BRIGHT + "Tạo đơn thành công! OrderID: " + orderId);
        return true;
    }

    // Xem lịch sử order
    public List<Order> getOrdersByCustomer(String customerId) {
        return orderDAO.findByCustomerId(customerId);
    }

    // xử lý cho staff
    public List<Order> getConfirmedOrders() {
        return orderDAO.findAll()
                .stream()
                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED)
                .toList();
    }

    public void updateOrderStatus(String orderId, OrderStatus status) {
        orderDAO.updateStatus(orderId, status);
    }
}