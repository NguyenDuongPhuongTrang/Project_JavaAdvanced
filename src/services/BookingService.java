package services;

import dao.*;
import enums.ComputerStatus;
import enums.PaymentMethod;
import enums.TransType;
import exceptions.InsufficientBalanceException;
import models.accounts.Customer;
import models.computer.Computer;
import models.trans.Booking;
import models.trans.Transaction;
import observer.TransactionSubject;
import strategy.*;
import utils.Config;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAOImpl();
    private ComputerDAO computerDAO = new ComputerDAOImpl();
    private CustomerDAO customerDAO = new CustomerDAOImpl();
    private TransactionService transactionService = new TransactionService();

    public void bookComputer(String customerId, String computerId, double hours) {

        Computer computer = computerDAO.findById(computerId);
        Customer customer = customerDAO.findById(customerId);

        if (computer == null || customer == null) {
            System.out.println(Config.RED + "\nKhông tìm thấy dữ liệu!" + Config.RESET);
            return;
        }

        if (!computer.isActive()) {
            System.out.println(Config.RED + "\nMáy không hoạt động!" + Config.RESET);
            return;
        }

        if (computer.getStatus() != ComputerStatus.AVAILABLE) {
            System.out.println(Config.RED + "\nMáy không khả dụng!" + Config.RESET);
            return;
        }

        // Strategy
        PricingStrategy strategy;
        strategy = new TotalPrice();

        double total = strategy.calculatePrice(hours, computer.getPrice());

        if (customer.getBalance() < total) {
            throw new InsufficientBalanceException(Config.RED + "\nSố dư không đủ để đặt máy!" + Config.RESET);
        }

        // UPDATE MÁY
        computerDAO.updateStatus(computerId, ComputerStatus.USING);

        // LƯU BOOKING
        LocalDateTime now = LocalDateTime.now();
        Booking booking = new Booking(
                generateBookingId(),
                customerId,
                computerId,
                hours,
                total,
                "ACTIVE",
                now,
                now.plusHours((long) hours)
        );

        bookingDAO.insert(booking);

        // TRANSACTION
        Transaction transaction = new Transaction(
                transactionService.generateTransactionId(),
                customerId,
                total,
                TransType.BOOKING,
                PaymentMethod.BALANCE,
                "Đặt máy " + computerId,
                LocalDateTime.now()
        );

        TransactionSubject.getInstance().notifyObservers(transaction);

        System.out.println(Config.GREEN_BRIGHT + "\nĐặt máy thành công!" + Config.RESET);
    }

    public String generateBookingId() {
        String lastId = bookingDAO.getLastBookingId();

        if (lastId == null || lastId.isEmpty()) {
            return "BK001";
        }

        int lastNumber = Integer.parseInt(lastId.substring(2));
        return "BK" + String.format("%03d", lastNumber + 1);
    }

    public void endBooking(String computerId) {

        Booking booking = bookingDAO.findActiveByComputerId(computerId);

        if (booking == null) {
            System.out.println(Config.RED + "\nKhông có phiên đang hoạt động!" + Config.RESET);
            return;
        }

        if (!"ACTIVE".equals(booking.getStatus())) {
            return;
        }

        bookingDAO.updateStatus(booking.getBookingId(), "COMPLETED");

        computerDAO.updateStatus(computerId, ComputerStatus.AVAILABLE);

        System.out.println(Config.GREEN_BRIGHT + "\nĐã kết thúc phiên hoạt động" + Config.RESET);
    }

    public void autoEndExpiredBookings() {

        List<Booking> list = bookingDAO.findAll();

        for (Booking b : list) {

            if (!"ACTIVE".equals(b.getStatus())) continue;

            if (b.getEndTime() == null) continue;

            if (LocalDateTime.now().isAfter(b.getEndTime())) {
                endBooking(b.getComputerId());
            }
        }
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }
}