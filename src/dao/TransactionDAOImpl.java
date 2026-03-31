package dao;

import enums.PaymentMethod;
import enums.TransType;
import models.trans.Transaction;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public void insert(Transaction transaction) {
        String sql = "INSERT INTO transactions (transactionId, customerId, amount, type, description, created_at, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transaction.getTransactionId());
            ps.setString(2, transaction.getCustomerId());
            ps.setDouble(3, transaction.getAmount());
            ps.setString(4, transaction.getType().name());
            ps.setString(5, transaction.getDescription());
            ps.setTimestamp(6, Timestamp.valueOf(transaction.getCreatedAt()));
            ps.setString(7, transaction.getPaymentMethod().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findByCustomerId(String customerId) {
        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE customerId = ? ORDER BY created_at DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getString("transactionId"),
                        rs.getString("customerId"),
                        rs.getDouble("amount"),
                        TransType.valueOf(rs.getString("type")),
                        PaymentMethod.valueOf(rs.getString("payment_method")),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );

                list.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public String getLastTransactionId() {
        String sql = "SELECT transactionId FROM transactions ORDER BY transactionId DESC LIMIT 1";

        try (
                Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement())
        {
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getString("transactionId");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public double getRevenueByDate(LocalDate date) {
        String sql = """
        SELECT SUM(amount)
        FROM transactions
        WHERE DATE(created_at) = ?
        AND type IN ('ORDER', 'BOOKING')
    """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, Date.valueOf(date));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public double getRevenueByMonth(int month, int year) {
        String sql = """
        SELECT SUM(amount)
        FROM transactions
        WHERE MONTH(created_at) = ?
        AND YEAR(created_at) = ?
        AND type IN ('ORDER', 'BOOKING')
    """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, month);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                list.add(new Transaction(
                        rs.getString("transactionId"),
                        rs.getString("customerId"),
                        rs.getDouble("amount"),
                        TransType.valueOf(rs.getString("type")),
                        PaymentMethod.valueOf(rs.getString("payment_method")),
                        rs.getString("description"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
