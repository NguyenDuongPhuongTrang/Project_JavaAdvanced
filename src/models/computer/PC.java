package models.computer;

import enums.ComputerStatus;
import enums.ComputerType;

public class PC {
    private String pcId;
    private String name;
    private double price;
    private ComputerStatus status;
    private ComputerType type;

    public PC(String pcId, String name, double price, ComputerStatus status, ComputerType type) {
        this.pcId = pcId;
        this.name = name;
        this.price = price;
        this.status = status;
        this.type = type;
    }

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
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

    public ComputerStatus getStatus() {
        return status;
    }

    public void setStatus(ComputerStatus status) {
        this.status = status;
    }

    public ComputerType getType() {
        return type;
    }

    public void setType(ComputerType type) {
        this.type = type;
    }
}
