package models.accounts;

public class Customer {
    private String customerId;
    private String userId;
    private String name;
    private double balance;

    public Customer(String customerId, String userId, String name, double balance) {
        this.customerId = customerId;
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
