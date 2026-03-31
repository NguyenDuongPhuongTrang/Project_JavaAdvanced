package models.computer;

import enums.ComputerStatus;
import enums.ComputerType;

public class Computer {
    private String computerId;
    private String name;
    private double price;
    private ComputerType type;
    private ComputerStatus status;
    private boolean active;

    public Computer(String computerId, String name, double price, ComputerType type, ComputerStatus status, boolean active) {
        this.computerId = computerId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.status = status;
        this.active = active;
    }

    public String getComputerId() {
        return computerId;
    }

    public void setComputerId(String computerId) {
        this.computerId = computerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ComputerType getType() {
        return type;
    }

    public void setType(ComputerType type) {
        this.type = type;
    }

    public ComputerStatus getStatus() {
        return status;
    }

    public void setStatus(ComputerStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
