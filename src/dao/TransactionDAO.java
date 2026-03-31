package dao;

import models.trans.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionDAO {
    void insert(Transaction transaction);
    List<Transaction> findByCustomerId(String customerId);
    String getLastTransactionId();
    double getRevenueByDate(LocalDate date);
    double getRevenueByMonth(int month, int year);
    List<Transaction> findAll();
}
