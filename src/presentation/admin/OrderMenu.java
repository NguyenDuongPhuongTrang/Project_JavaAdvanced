package presentation.admin;

import models.orders.Order;
import services.OrderService;
import utils.Config;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;

public class OrderMenu {
    private OrderService orderService = new OrderService();
    public void showOrder() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println(Config.YELLOW + "\nChưa có order nào!" + Config.RESET);
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (Order o : orders) {
            rows.add(new String[]{
                    o.getOrderId(),
                    o.getCustomerId(),
                    o.getBookingId() != null ? o.getBookingId() : "MANUAL",
                    String.format("%,.0fđ", o.getTotalAmount()),
                    o.getStatus().name(),
                    o.getCreatedAt().toString()
            });
        }

        TablePrinter.printTableWithPagination(
                "DANH SÁCH ORDER",
                new String[]{"ORDER_ID", "CUSTOMER_ID" ,"BOOKING_ID", "TOTAL", "STATUS", "CREATED_AT"},
                rows, 10
        );
    }
}
