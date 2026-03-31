package presentation;

import dao.*;
import enums.OrderStatus;
import enums.TransType;
import models.accounts.Customer;
import models.computer.Computer;
import models.orders.Order;
import models.orders.OrderItem;
import models.products.MenuItems;
import models.trans.Booking;
import presentation.admin.DisplayItemMenu;
import services.*;
import utils.Config;
import utils.Input;
import utils.TablePrinter;

import java.util.*;

public class StaffMenu {

    private final StaffService staffService = new StaffService();
    private final BookingDAO bookingDAO = new BookingDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
    private final ComputerService computerService = new ComputerService();
    private final CustomerService customerService = new CustomerService();
    private final OrderService orderService = new OrderService();
    private final ItemDAO itemDao = new ItemDAOImpl();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final BookingService bookingService = new BookingService();
    // dùng lại service đã có

    private final DisplayItemMenu displayItemMenu = new DisplayItemMenu();

    public void showMenu() {
        while (true) {
            System.out.println(Config.BLUE_BRIGHT + "\n===== STAFF MENU =====" + Config.RESET);
            System.out.println("1. Xem danh sách order");
            System.out.println("2. Cập nhật trạng thái order");
            System.out.println("3. Xuất hóa đơn theo máy");
            System.out.println("4. Xem danh sách máy");
            System.out.println("5. Kết thúc phiên hoạt động sớm");
            System.out.println("6. Thêm đơn hàng");
            System.out.println("0. Đăng xuất");

            int choice = Input.inputIntegerPositive("Chọn: ");

            switch (choice) {
                case 1 -> viewOrders();
                case 2 -> updateOrderStatus();
                case 3 -> printInvoice();
                case 4 -> viewComputers();
                case 5 -> endEarly();
                case 6 -> createManualOrder();
                case 0 -> {
                    return;
                }
                default -> System.out.println(Config.RED + "Lựa chọn không hợp lệ!" + Config.RESET);
            }
        }
    }

    // Xem danh sách order
    public void viewOrders() {
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

    // Cập nhật trạng thái order
    public void updateOrderStatus() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== CẬP NHẬT TRẠNG THÁI ORDER ===" + Config.RESET);
        System.out.print("Nhập OrderID: ");
        String orderId = Input.inputString("").trim();

        Order order = staffService.getOrderById(orderId);
        if (order == null) {
            return;
        }

        System.out.println("Trạng thái hiện tại: " + Config.CYAN + order.getStatus() + Config.RESET);

        System.out.println("\n1. CONFIRMED → SERVING");
        System.out.println("2. SERVING → COMPLETED");
        System.out.println("0. Hủy");

        int choice = Input.inputIntegerPositive("Chọn: ");

        OrderStatus newStatus = null;
        switch (choice) {
            case 1 -> newStatus = OrderStatus.SERVING;
            case 2 -> newStatus = OrderStatus.COMPLETED;
            case 0 -> { return; }
            default -> {
                System.out.println(Config.RED + "Lựa chọn không hợp lệ!" + Config.RESET);
                return;
            }
        }

        boolean success = staffService.updateOrderStatus(orderId, newStatus);
        if (success) {
            System.out.println(Config.GREEN_BRIGHT + "\nCập nhật trạng thái thành công!" + Config.RESET);
        } else {
            System.out.println(Config.RED + "\nCập nhật thất bại!" + Config.RESET);
        }
    }

    // In hóa đơn
    public void printInvoice() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== XUẤT HÓA ĐƠN ===" + Config.RESET);
        System.out.println("1. Hóa đơn theo phiên chơi");
        System.out.println("2. Hóa đơn đơn lẻ");
        System.out.println("0. Hủy");
        int choice = Input.inputIntegerPositive("Chọn: ");
        switch (choice) {
            case 1 -> printInvoiceByComputer();
            case 2 -> printInvoiceByOrder();
            case 0 -> { return; }
            default -> System.out.println(Config.RED + "Lựa chọn không hợp lệ!" + Config.RESET);
        }
    }

    public void printInvoiceByComputer() {
        System.out.print("Nhập ID máy: ");
        String computerId = Input.inputString("").trim();

        Booking booking = bookingDAO.findActiveByComputerId(computerId);
        if (booking == null) {
            System.out.println(Config.YELLOW + "Không tìm thấy phiên chơi!" + Config.RESET);
            return;
        }

        String customerId = booking.getCustomerId();
        Customer customer = (customerId != null && !customerId.startsWith("GUEST"))
                ? new CustomerDAOImpl().findById(customerId)
                : null;

        // Lấy tất cả order trong khoảng thời gian phiên
        List<Order> orders = orderDAO.findByCustomerId(customerId).stream()
                .filter(o -> !o.getCreatedAt().isBefore(booking.getStartTime()) &&
                        !o.getCreatedAt().isAfter(booking.getEndTime()))
                .toList();

        double totalFood = 0;
        double computerMoney = booking.getTotalAmount();

        System.out.println("\n" + Config.CYAN + "===== HÓA ĐƠN THEO MÁY =====" + Config.RESET);
        System.out.println("Khách hàng : " + (customerId != null ? customerId : "Vãng lai"));
        System.out.println("Máy        : " + booking.getComputerId());
        System.out.println("Bắt đầu    : " + booking.getStartTime());
        System.out.println("Kết thúc   : " + booking.getEndTime());

        System.out.println("\n" + Config.YELLOW + "--- ĐỒ ĂN / NƯỚC ---" + Config.RESET);
        for (Order o : orders) {
            List<OrderItem> items = orderItemDAO.findByOrderId(o.getOrderId());
            for (OrderItem item : items) {
                MenuItems menuItem = itemDao.findById(item.getItemId());
                String itemName = (menuItem != null) ? menuItem.getItemName() : item.getItemId();
                double subtotal = item.getQuantity() * item.getPrice();
                totalFood += subtotal;

                System.out.printf("%-20s x%-2d | %,10.0fđ%n",
                        itemName, item.getQuantity(), subtotal);
            }
        }

        System.out.println("\n" + Config.CYAN + "------------------------------" + Config.RESET);
        System.out.printf("Tiền máy   : %,15.0fđ%n", computerMoney);
        System.out.printf("Tiền F&B   : %,15.0fđ%n", totalFood);

        double grandTotal = computerMoney + totalFood;
        System.out.println(Config.GREEN_BRIGHT +
                String.format("TỔNG CỘNG : %,15.0fđ", grandTotal)
                + Config.RESET);

        if (customer != null) {
            String method = (customer.getBalance() >= grandTotal) ? "TỰ ĐỘNG TRỪ TK" : "TIỀN MẶT";
            System.out.println(Config.CYAN + "Phương thức thanh toán: " + method + Config.RESET);
        } else {
            System.out.println(Config.CYAN + "Phương thức thanh toán: TIỀN MẶT" + Config.RESET);
        }

        System.out.println(Config.CYAN + "==============================" + Config.RESET);
    }

    public void printInvoiceByOrder() {
        System.out.print("Nhập OrderID: ");
        String orderId = Input.inputString("").trim();

        Order order = orderDAO.findById(orderId);
        if (order == null) {
            System.out.println(Config.YELLOW + "Không tìm thấy Order!" + Config.RESET);
            return;
        }

        String customerId = order.getCustomerId();
        Customer customer = (customerId != null && !customerId.startsWith("GUEST"))
                ? new CustomerDAOImpl().findById(customerId)
                : null;

        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);

        double totalFood = 0;
        for (OrderItem item : items) {
            MenuItems menuItem = itemDao.findById(item.getItemId());
            String itemName = (menuItem != null) ? menuItem.getItemName() : item.getItemId();
            double subtotal = item.getQuantity() * item.getPrice();
            totalFood += subtotal;
        }

        System.out.println("\n" + Config.CYAN + "===== HÓA ĐƠN THEO ĐƠN =====" + Config.RESET);
        System.out.println("OrderID    : " + order.getOrderId());
        System.out.println("Khách hàng : " + (customerId != null ? customerId : "Vãng lai"));
        System.out.println("Trạng thái : " + order.getStatus());

        System.out.println("\n" + Config.YELLOW + "--- ĐỒ ĂN / NƯỚC ---" + Config.RESET);
        for (OrderItem item : items) {
            MenuItems menuItem = itemDao.findById(item.getItemId());
            String itemName = (menuItem != null) ? menuItem.getItemName() : item.getItemId();
            double subtotal = item.getQuantity() * item.getPrice();
            System.out.printf("%-20s x%-2d | %,10.0fđ%n",
                    itemName, item.getQuantity(), subtotal);
        }

        System.out.println("\n" + Config.CYAN + "------------------------------" + Config.RESET);
        System.out.printf("Tiền F&B   : %,15.0fđ%n", totalFood);
        System.out.println(Config.GREEN_BRIGHT +
                String.format("TỔNG CỘNG   : %,15.0fđ", totalFood)
                + Config.RESET);

        if (customer != null) {
            String method = (customer.getBalance() >= totalFood) ? "TỰ ĐỘNG TRỪ TK" : "TIỀN MẶT";
            System.out.println(Config.CYAN + "Phương thức thanh toán: " + method + Config.RESET);
        } else {
            System.out.println(Config.CYAN + "Phương thức thanh toán: TIỀN MẶT" + Config.RESET);
        }

        System.out.println(Config.CYAN + "==============================" + Config.RESET);
    }

    // Thêm đơn hàng không theo phiên
    public void createManualOrder() {
        System.out.println("\n" + Config.BLUE_BRIGHT + "=== THÊM ĐƠN HÀNG THỦ CÔNG ===" + Config.RESET);
        System.out.println("1. Theo phiên chơi đang hoạt động");
        System.out.println("2. Đơn hàng ngoài phiên");
        System.out.println("0. Hủy");

        int type = Input.inputIntegerPositive("Chọn loại đơn: ");
        if (type == 0) return;

        String bookingId = null;
        String computerId = null;
        String customerId = null;
        Customer customer = null;

        if (type == 1) {
            System.out.print("Nhập ID máy: ");
            computerId = Input.inputString("").trim();
            Booking booking = bookingDAO.findActiveByComputerId(computerId);
            if (booking == null) {
                System.out.println(Config.RED + "Không có phiên chơi hoạt động trên máy này!" + Config.RESET);
                return;
            }
            bookingId = booking.getBookingId();
            customerId = booking.getCustomerId();
        } else if (type == 2) {
            System.out.print("Nhập ID khách hàng (để trống nếu là đơn lẻ): ");
            customerId = Input.input("").trim();
            if (customerId.isEmpty()) {
                customerId = customerService.generateGuestCustomerId();
                System.out.println(Config.YELLOW + "ĐƠN LẺ" + Config.RESET);
            }
        }

        // Lấy thông tin customer nếu có
        if (customerId != null && !customerId.startsWith("GUEST")) {
            customer = customerDAO.findById(customerId);
            if (customer == null) {
                System.out.println(Config.RED + "\nKhách hàng không tồn tại!" + Config.RESET);
                return;
            }
        }

        // Chọn món
        Map<MenuItems, Integer> orderItems = selectItemsForOrder();
        if (orderItems.isEmpty()) {
            System.out.println(Config.YELLOW + "\nKhông có món nào được chọn." + Config.RESET);
            return;
        }

        double total = calculateTotal(orderItems);
        System.out.printf("\nTổng tiền đơn hàng: " + Config.GREEN_BRIGHT + "%,.0fđ" + Config.RESET + "\n", total);
        System.out.print("Xác nhận tạo đơn? (1: Có / 0: Hủy): ");
        if (Input.inputIntegerPositive("") != 1) return;

        // Tạo order
        boolean success = orderService.createManualOrder(customerId, bookingId, computerId, orderItems);
        if (!success) {
            System.out.println(Config.RED + "\nTạo đơn thất bại!" + Config.RESET);
            return;
        }

        // Thanh toán
        if (customer != null) {
            if (customer.getBalance() >= total) {
                System.out.println(Config.GREEN_BRIGHT + "Đã tự động trừ tiền từ tài khoản khách." + Config.RESET);
            } else {
                System.out.println(Config.YELLOW + "Số dư không đủ, khách dùng tiền mặt." + Config.RESET);
            }
        }
    }

    // Chọn nhiều món
    private Map<MenuItems, Integer> selectItemsForOrder() {
        Map<MenuItems, Integer> orderItems = new LinkedHashMap<>();
        List<MenuItems> availableItems = displayItemMenu.displayItemsForCustomer();

        while (true) {
            System.out.print("\nID món (0 để hoàn tất): ");
            String id = Input.inputString("").trim();
            if (id.equals("0")) break;

            MenuItems item = availableItems.stream()
                    .filter(i -> i.getItemId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);

            if (item == null) {
                System.out.println(Config.RED + "\nMón không tồn tại!" + Config.RESET);
                continue;
            }

            System.out.print("Số lượng: ");
            int qty = Input.inputIntegerPositive("");
            if (qty > 0) {
                orderItems.merge(item, qty, Integer::sum);
                System.out.println(Config.GREEN_BRIGHT + "Đã thêm " + item.getItemName() + " x " + qty + Config.RESET);
            }
        }
        return orderItems;
    }

    private double calculateTotal(Map<MenuItems, Integer> items) {
        return items.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }

    private void endEarly() {
        System.out.print("Nhập ID máy cần kết thúc: ");
        String computerId = Input.inputString("");
        bookingService.endBooking(computerId);
    }

    private void viewComputers() {
        List<Computer> computersToDisplay = computerService.getAllComputers();
        List<String[]> rows = new ArrayList<>();

        if (computersToDisplay == null || computersToDisplay.isEmpty()) {
            System.out.println(Config.YELLOW + "\nDanh sách PC đang trống!" + Config.RESET);
            return;
        }

        for (Computer c : computersToDisplay) {
            Booking current = computerService.getCurrentBooking(c.getComputerId());

            String user = "-";
            String time = "-";

            if (current != null) {
                user = current.getCustomerId();
                time = current.getStartTime().toLocalTime() + " - " + current.getEndTime().toLocalTime();
            }

            rows.add(new String[]{
                    c.getComputerId(),
                    c.getName(),
                    String.valueOf(c.getPrice()),
                    c.getType().toString(),
                    c.getStatus().toString(),
                    user,
                    time
            });
        }

        TablePrinter.printTableWithPagination(
                "DANH SÁCH PC",
                new String[]{
                        "ID", "NAME", "PRICE", "TYPE", "STATUS", "USER", "TIME"
                },
                rows,
                5
        );
    }
}