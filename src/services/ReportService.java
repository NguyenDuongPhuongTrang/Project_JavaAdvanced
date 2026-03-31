package services;

import dao.TransactionDAO;
import dao.TransactionDAOImpl;

import java.time.LocalDate;

public class ReportService {
    private TransactionDAO transactionDAO = new TransactionDAOImpl();

    public double getRevenueByDate(LocalDate date) {
        return transactionDAO.getRevenueByDate(date);
    }

    public double getRevenueByMonth(int month, int year) {
        return transactionDAO.getRevenueByMonth(month, year);
    }
}
