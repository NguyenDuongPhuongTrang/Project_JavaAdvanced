package dao;

import models.accounts.Staff;

import java.sql.Connection;
import java.util.List;

public interface StaffDAO {
    void insert(Connection conn, Staff staff);
    String getLastStaffId();
    void deleteByUserId(Connection conn, String userId);
    List<Staff> findAll();
}
