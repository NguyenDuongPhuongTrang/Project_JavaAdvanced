package dao;

import enums.ComputerStatus;
import enums.ComputerType;
import models.computer.Computer;
import utils.Config;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComputerDAOImpl implements ComputerDAO {
    private Connection conn = DBConnection.getConnection();

    public String getLastComputerId() {
        String sql = "SELECT computerId FROM computers ORDER BY computerId DESC LIMIT 1";
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("computerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(Computer computer) {
        String sql = "INSERT INTO computers(computerId, computerName, price, type, status, is_active) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, computer.getComputerId());
            ps.setString(2, computer.getName());
            ps.setDouble(3, computer.getPrice());
            ps.setString(4, computer.getType().name());
            ps.setString(5, computer.getStatus().name());
            ps.setBoolean(6, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Computer findById(String computerId) {
        String sql = "SELECT * FROM computers WHERE computerId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, computerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Computer(
                        rs.getString("computerId"),
                        rs.getString("computerName"),
                        rs.getDouble("price"),
                        ComputerType.valueOf(rs.getString("type")),
                        ComputerStatus.valueOf(rs.getString("status")),
                        rs.getBoolean("is_active")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Computer> findAll() {
        List<Computer> list = new ArrayList<>();
        String sql = "SELECT * FROM computers";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Computer(
                        rs.getString("computerId"),
                        rs.getString("computerName"),
                        rs.getDouble("price"),
                        ComputerType.valueOf(rs.getString("type")),
                        ComputerStatus.valueOf(rs.getString("status")),
                        rs.getBoolean("is_active")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Computer> findAvailable() {
        List<Computer> list = new ArrayList<>();
        String sql = "SELECT * FROM computers WHERE is_active = true";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Computer(
                        rs.getString("computerId"),
                        rs.getString("computerName"),
                        rs.getDouble("price"),
                        ComputerType.valueOf(rs.getString("type")),
                        ComputerStatus.valueOf(rs.getString("status")),
                        rs.getBoolean("is_active")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updateStatus(String computerId, ComputerStatus status) {
        String sql = "UPDATE computers SET status=? WHERE computerId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, computerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePrice(String computerId, double price) {
        String sql = "UPDATE computers SET price=? WHERE computerId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, price);
            ps.setString(2, computerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateType(String computerId, ComputerType type) {
        String sql = "UPDATE computers SET type=? WHERE computerId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.name());
            ps.setString(2, computerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateName(String computerId, String name) {
        String sql = "UPDATE computers SET computerName=? WHERE computerId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, computerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(String computerId) {
        String sql = "UPDATE computers SET is_active = false WHERE computerId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, computerId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
