package dao;

import models.accounts.Staff;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAOImpl implements StaffDAO {

    @Override
    public void insert(Connection conn, Staff staff) {
        String sql = "INSERT INTO staffs(staffId, userId, name, phone) VALUES (?, ?, ?, ?)";

        try (
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, staff.getStaffId());
            ps.setString(2, staff.getUserId());
            ps.setString(3, staff.getName());
            ps.setString(4, staff.getPhone());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLastStaffId() {
        String sql = "SELECT staffId FROM staffs ORDER BY staffId DESC LIMIT 1";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            if (rs.next()) {
                return rs.getString("staffId");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void deleteByUserId(Connection conn, String userId) {
        String sql = "DELETE FROM staffs WHERE userId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Staff> findAll() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staffs";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                list.add(new Staff(
                        rs.getString("staffId"),
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
