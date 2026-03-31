package presentation;

import dao.BookingDAOImpl;
import dao.TransactionDAOImpl;
import enums.ComputerStatus;
import enums.TransType;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import models.accounts.Customer;
import models.accounts.User;
import models.computer.Computer;
import models.products.MenuItems;
import models.trans.Booking;
import models.trans.Transaction;
import presentation.admin.DisplayItemMenu;
import services.*;
import utils.Config;
import utils.Input;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomerMenu {
    private CustomerService customerService = new CustomerService();
    private Customer currentCustomer;

    public CustomerMenu(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    private BookingService bookingService = new BookingService();
    private ComputerService computerService = new ComputerService();

    public void showMenu() {
        while (true) {
            refreshCustomer();
            bookingService.autoEndExpiredBookings();
            System.out.println(Config.BLUE_BRIGHT + "\n==== CUSTOMER MENU ====" + Config.RESET);
            System.out.println("1. Nạp tiền");
            System.out.println("2. Đặt máy trạm");
            System.out.println("3. Đặt món ăn");
            System.out.println("4. Theo dõi số dư tài khoản");
            System.out.println("5. Xem lịch sử giao dịch");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            int choice = Input.inputIntegerPositive("");
            switch (choice) {
                case 1:
                    deposit();
                    break;
                case 2:
                    bookComputer();
                    break;
                case 3:
                    placeOrderUI();
                    break;
                case 4:
                    showBalance();
                    break;
                case 5:
                    showTransactionHistory();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Config.RED + "Lựa chọn không hợp lệ!" + Config.RESET);
                    break;
            }
        }
    }

    // Nạp tiền
    private void deposit() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== NẠP TIỀN ===" + Config.RESET);
        while (true) {
            try {
                System.out.print("Nhập số tiền: ");
                double amount = Input.inputDoublePositive("");
                customerService.deposit(currentCustomer.getCustomerId(), amount);
                break;
            } catch (ValidationException e){
                System.out.println(e.getMessage());
            }
        }
    }

    // đặt máy
    private void bookComputer() {
        List<Computer> list = computerService.getAvailableComputers();
        List<String[]> rows = new ArrayList<>();

        if (list.isEmpty()) {
            System.out.println(Config.YELLOW + "\nKhông có máy hoạt động!" + Config.RESET);
            return;
        }

        for (Computer c : list) {
            rows.add(new String[]{
                    c.getComputerId(),
                    c.getType().toString(),
                    c.getPrice() + "đ/h",
                    c.getStatus().toString()
            });
        }

        TablePrinter.printTableWithPagination(
                "DANH SÁCH MÁY HOẠT ĐỘNG",
                new String[]{"ID", "TYPE", "PRICE", "STATUS"},
                rows,
                5
        );

        Computer selected = null;

        System.out.println(Config.BLUE_BRIGHT + "\n=== ĐẶT MÁY ===" + Config.RESET);

        while (true) {
            System.out.print("Nhập ID máy: ");
            String id = Input.inputString("");

            selected = list.stream()
                    .filter(c -> c.getComputerId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);

            if (selected == null) {
                System.out.println(Config.RED + "Máy không tồn tại!" + Config.RESET);
                continue;
            }

            if (!selected.isActive()) {
                System.out.println(Config.RED + "Máy đã ngừng hoạt động!" + Config.RESET);
                continue;
            }

            if (selected.getStatus() == ComputerStatus.USING) {
                System.out.println(Config.RED + "Máy đang được sử dụng!" + Config.RESET);
                continue;
            }

            if (selected.getStatus() == ComputerStatus.MAINTENANCE) {
                System.out.println(Config.RED + "Máy đang bảo trì!" + Config.RESET);
                continue;
            }

            break;
        }

        // nhập giờ
        System.out.print("Nhập số giờ: ");
        double hours = Input.inputDoublePositive("");

        // gọi service
        bookingService.bookComputer(
                currentCustomer.getCustomerId(),
                selected.getComputerId(),
                hours
        );
        currentCustomer = customerService.findById(currentCustomer.getCustomerId());
    }

    // đặt đồ ăn
    public void placeOrderUI() {
        Map<MenuItems, Integer> orderItems = new LinkedHashMap<>();
        DisplayItemMenu displayItemMenu = new DisplayItemMenu();
        List<MenuItems> availableItems = displayItemMenu.displayItemsForCustomer();

        System.out.println(Config.BLUE_BRIGHT + "\n=== ĐẶT MÓN ===" + Config.RESET);

        while (true) {
            System.out.print("\nID món muốn order (0 để hoàn tất): ");
            String id = Input.inputString("").trim();

            if (id.equals("0")) break;

            MenuItems item = availableItems.stream()
                    .filter(i -> i.getItemId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);

            if (item == null) {
                System.out.println(Config.RED + "Món không tồn tại!" + Config.RESET);
                continue;
            }

            System.out.print("Nhập số lượng: ");
            int qty = Input.inputIntegerPositive("");
            if (qty <= 0) continue;

            orderItems.merge(item, qty, Integer::sum);
            System.out.println(Config.GREEN + "Đã thêm: " + item.getItemName() + " x " + qty + Config.RESET);
        }

        if (orderItems.isEmpty()) {
            System.out.println(Config.YELLOW + "\nBạn chưa chọn món nào." + Config.RESET);
            return;
        }

        // Hiển thị lại đơn hàng + tổng tiền trước khi đặt
        System.out.println("\n" + Config.YELLOW + "\n=== XÁC NHẬN ĐƠN HÀNG ===" + Config.RESET);
        double total = 0;
        for (var entry : orderItems.entrySet()) {
            double subtotal = entry.getKey().getPrice() * entry.getValue();
            total += subtotal;
            System.out.printf("%s x %d = %.0fđ%n",
                    entry.getKey().getItemName(), entry.getValue(), subtotal);
        }
        System.out.println("Tổng tiền: " + Config.GREEN_BRIGHT + total + "đ" + Config.RESET);

        System.out.print("\nXác nhận đặt món? (1: Có / 0: Hủy): ");
        if (Input.inputIntegerPositive("") != 1) {
            System.out.println(Config.YELLOW + "Đã hủy đặt món." + Config.RESET);
            return;
        }

        // Gọi service
        OrderService orderService = new OrderService();
        Booking booking = new BookingDAOImpl().findActiveByCustomerId(currentCustomer.getCustomerId());

        boolean success = orderService.placeOrder(booking.getComputerId(), orderItems);
        if (success) {
            currentCustomer = customerService.findById(currentCustomer.getCustomerId());
        }
    }

    // Xem số dư tài khoản
    private void showBalance() {
        currentCustomer = customerService.findById(currentCustomer.getCustomerId());
        System.out.println(Config.BLUE_BRIGHT + "\n=== SỐ DƯ TÀI KHOẢN ===" + Config.RESET);
        System.out.println("Số dư: " + currentCustomer.getBalance());
    }

    // Xem lịch sử giao dịch
    private void showTransactionHistory() {
        List<Transaction> transactions = new TransactionDAOImpl()
                .findByCustomerId(currentCustomer.getCustomerId());

        if (transactions.isEmpty()) {
            System.out.println(Config.YELLOW + "\nBạn chưa có giao dịch nào!" + Config.RESET);
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (Transaction t : transactions) {
            String sign = (t.getType() == TransType.DEPOSIT) ? "+" : "-";
            String amountStr = sign + String.format("%.0fđ", t.getAmount());
            rows.add(new String[]{
                    t.getTransactionId(),
                    t.getType().name(),
                    amountStr,
                    t.getDescription(),
                    t.getCreatedAt().toString()
            });
        }

        TablePrinter.printTableWithPagination(
                "LỊCH SỬ GIAO DỊCH",
                new String[]{"ID", "LOẠI", "SỐ TIỀN", "MÔ TẢ", "THỜI GIAN"},
                rows,
                5
        );
    }

    // reload
    private void refreshCustomer() {
        currentCustomer = customerService.findById(currentCustomer.getCustomerId());
    }
}
