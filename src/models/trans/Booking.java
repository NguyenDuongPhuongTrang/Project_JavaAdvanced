package models.trans;

import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private String customerId;
    private String computerId;
    private double hours;
    private double totalAmount;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Booking(String bookingId, String customerId, String computerId, double hours, double totalAmount, String status, LocalDateTime startTime, LocalDateTime endTime) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.computerId = computerId;
        this.hours = hours;
        this.totalAmount = totalAmount;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getComputerId() {
        return computerId;
    }

    public void setComputerId(String computerId) {
        this.computerId = computerId;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
