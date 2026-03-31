package models.products;

import enums.ItemType;

public class MenuItems {
    private String itemId;
    private String itemName;
    private double price;
    private ItemType category;
    private boolean active;

    public MenuItems(String itemId, String itemName, double price, ItemType category, boolean active) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.active = active;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ItemType getCategory() {
        return category;
    }

    public void setCategory(ItemType category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
