package observer;

import dao.CustomerDAO;
import dao.CustomerDAOImpl;
import dao.TransactionDAO;
import dao.TransactionDAOImpl;
import enums.PaymentMethod;
import models.accounts.Customer;
import models.trans.Transaction;

public class TransactionLogger implements TransactionObserver {

    private TransactionDAO transactionDAO = new TransactionDAOImpl();
    private CustomerDAO customerDAO = new CustomerDAOImpl();

    @Override
    public void update(Transaction transaction) {

        // lưu transaction
        transactionDAO.insert(transaction);

        // Nếu không có customerid
        if (transaction.getCustomerId() == null || transaction.getCustomerId().trim().isEmpty()) {
            return;
        }

        // xử lý số dư
        Customer customer = customerDAO.findById(transaction.getCustomerId());
        if (customer == null) return;

        switch (transaction.getType()) {
            case DEPOSIT -> customer.setBalance(customer.getBalance() + transaction.getAmount());
            case ORDER, BOOKING -> {
                if (transaction.getPaymentMethod() == PaymentMethod.BALANCE) {
                    customer.setBalance(customer.getBalance() - transaction.getAmount());
                }
            }
        }

        customerDAO.update(customer);
    }
}