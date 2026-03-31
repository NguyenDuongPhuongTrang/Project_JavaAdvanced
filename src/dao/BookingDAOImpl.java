package dao;

import models.trans.Booking;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    @Override
    public void insert(Booking booking) {
        String sql = "INSERT INTO bookings VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, booking.getBookingId());
            ps.setString(2, booking.getCustomerId());
            ps.setString(3, booking.getComputerId());
            ps.setDouble(4, booking.getHours());
            ps.setDouble(5, booking.getTotalAmount());
            ps.setString(6, booking.getStatus());
            ps.setObject(7, booking.getStartTime());
            ps.setObject(8, booking.getEndTime());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Booking> findByCustomerId(String customerId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE customerId = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Booking(
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getString("computerId"),
                        rs.getDouble("hours"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public String getLastBookingId() {
        String sql = "SELECT bookingId FROM bookings ORDER BY bookingId DESC LIMIT 1";

        try (
                Connection conn = DBConnection.getConnection();
                Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getString("bookingId");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                list.add(new Booking(
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getString("computerId"),
                        rs.getDouble("hours"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Booking findById(String bookingId) {
        String sql = "SELECT * FROM bookings WHERE bookingId = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Booking(
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getString("computerId"),
                        rs.getDouble("hours"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Booking findActiveByComputerId(String computerId) {
        String sql = """
        SELECT * FROM bookings
        WHERE computerId = ?
        AND status = 'ACTIVE'
    """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, computerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Booking(
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getString("computerId"),
                        rs.getDouble("hours"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Booking findActiveByCustomerId(String customerId) {
        String sql = """
        SELECT * FROM bookings
        WHERE customerId = ?
        AND status = 'ACTIVE'
        AND NOW() BETWEEN startTime AND endTime
    """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Booking(
                        rs.getString("bookingId"),
                        rs.getString("customerId"),
                        rs.getString("computerId"),
                        rs.getDouble("hours"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateStatus(String bookingId, String status) {

        String sql = "UPDATE bookings SET status = ? WHERE bookingId = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            ps.setString(2, bookingId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
