package models.orders;

import enums.OrderStatus;

import java.time.LocalDateTime;

public class Order {
    private String orderId;
    private String bookingId;
    private String customerId;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public Order(String orderId, String bookingId, String customerId,
                 double totalAmount, OrderStatus status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order(String orderId, String customerId, double totalAmount,
                 OrderStatus status, LocalDateTime createdAt) {
        this(orderId, null, customerId, totalAmount, status, createdAt);
    }

    public boolean isManualOrder() {
        return bookingId == null || bookingId.trim().isEmpty();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
