package dao;

import models.trans.Booking;

import java.util.List;

public interface BookingDAO {
    void insert(Booking booking);
    List<Booking> findByCustomerId(String customerId);
    String getLastBookingId();
    List<Booking> findAll();
    Booking findById(String bookingId);
    Booking findActiveByComputerId(String computerId);
    Booking findActiveByCustomerId(String customerId);
    void updateStatus(String bookingId, String status);
}
