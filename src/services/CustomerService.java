package services;

import dao.CustomerDAO;
import dao.CustomerDAOImpl;
import dao.TransactionDAO;
import dao.TransactionDAOImpl;
import enums.PaymentMethod;
import enums.TransType;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import models.accounts.Customer;
import models.trans.Transaction;
import observer.TransactionSubject;
import utils.Config;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerService {
    CustomerDAO customerDAO = new CustomerDAOImpl();
    private TransactionService transactionService = new TransactionService();

    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    public Customer findById(String id) {
        return customerDAO.findById(id);
    }

    public void deposit(String customerId, double amount) {
        Customer customer = customerDAO.findById(customerId);

        if (amount == 0){
            throw new ValidationException(Config.RED + "Số tiền phải lớn hơn 0!" + Config.RESET);
        }

        Transaction transaction = new Transaction(
                transactionService.generateTransactionId(),
                customerId,
                amount,
                TransType.DEPOSIT,
                PaymentMethod.BALANCE,
                "Nạp tiền",
                LocalDateTime.now()
        );

        TransactionSubject.getInstance().notifyObservers(transaction);

        System.out.println(Config.GREEN_BRIGHT + "\nNạp tiền thành công!" + Config.RESET);
    }

    public Customer getByUserId(String userId) {
        Customer customer = customerDAO.findByUserId(userId);

        if (customer == null) {
            throw new NotFoundException(Config.RED + "\nKhông tìm thấy khách hàng!" + Config.RESET);
        }

        return customer;
    }

    public String generateGuestCustomerId() {
        String lastId = customerDAO.getLastGuestCustomerId();

        if (lastId == null || !lastId.startsWith("GUEST")) {
            return "GUEST001";
        }

        try {
            int lastNumber = Integer.parseInt(lastId.substring(5)); // cắt bỏ "GUEST"
            return "GUEST" + String.format("%03d", lastNumber + 1);
        } catch (Exception e) {
            return "GUEST001";
        }
    }
}
