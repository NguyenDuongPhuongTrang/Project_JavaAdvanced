package services;

import dao.TransactionDAO;
import dao.TransactionDAOImpl;
import models.trans.Transaction;

import java.util.List;

public class TransactionService {
    TransactionDAO transactionDAO = new TransactionDAOImpl();

    public String generateTransactionId() {
        String lastId = transactionDAO.getLastTransactionId();
        if (lastId == null || lastId.isEmpty()) {
            return "TR001";
        }
        int lastNumber = Integer.parseInt(lastId.substring(2));
        return "TR" + String.format("%03d", lastNumber + 1);
    }

    public List<Transaction> getByCustomerId(String customerId) {
        return transactionDAO.findByCustomerId(customerId);
    }

    public List<Transaction> getAll() {
        return transactionDAO.findAll();
    }
}
