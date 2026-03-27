package models.orders;

import enums.ItemType;

public class OrderItem {
    private String orderItemsId;
    private String orderId;
    private String itemId;
    private int quantity;
    private double price;

    public OrderItem(String orderItemsId, String orderId, String itemId, int quantity, double price) {
        this.orderItemsId = orderItemsId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderItemsId() {
        return orderItemsId;
    }

    public void setOrderItemsId(String orderItemsId) {
        this.orderItemsId = orderItemsId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
