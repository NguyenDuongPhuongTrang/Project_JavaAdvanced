package dao;

import models.accounts.Customer;

import java.sql.Connection;
import java.util.List;

public interface CustomerDAO {
    void insert(Connection conn, Customer customer);
    String getLastCustomerId();
    void deleteByUserId(Connection conn, String userId);
    List<Customer> findAll();
    Customer findById(String id);
    void update(Customer customer);
    Customer findByUserId(String userId);
    String getLastGuestCustomerId();
}
