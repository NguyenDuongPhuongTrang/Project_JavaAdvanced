package services;

import dao.ItemDAO;
import dao.ItemDAOImpl;
import enums.ItemType;
import exceptions.NotFoundException;
import models.computer.Computer;
import models.products.MenuItems;
import utils.Config;

import java.util.List;

public class ItemService {
    ItemDAO itemDAO = new ItemDAOImpl();

    // Generate ID
    public String generateItemId() {
        String lastId = itemDAO.getLastItemId();
        int next = (lastId != null) ? Integer.parseInt(lastId.substring(1)) + 1 : 1;
        return String.format("P%03d", next);
    }

    // Thêm món
    public void addItem(String name, double price, ItemType category) {
        String id = generateItemId();
        MenuItems item = new MenuItems(id, name, price, category, true);

        itemDAO.insert(item);

        System.out.println(Config.GREEN_BRIGHT + "\nThêm món thành công!" + Config.RESET);
    }

    // Cập nhật món
    public void updateItem(String itemId, String name, Double price, ItemType category) {
        MenuItems item = findItemById(itemId);
        if (name != null && !name.trim().isEmpty()) {
            item.setItemName(name);
        }

        if (price != null) {
            item.setPrice(price);
        }

        if (category != null) {
            item.setCategory(category);
        }

        itemDAO.update(item);

        System.out.println(Config.GREEN_BRIGHT + "\nCập nhật thành công!" + Config.RESET);
    }

    // Xóa món
    public void deleteItem(String itemId) {
        findItemById(itemId);
        itemDAO.delete(itemId);
        System.out.println(Config.GREEN_BRIGHT + "\nXóa món thành công!" + Config.RESET);
    }

    // Check món tồn tại
    public MenuItems findItemById(String itemId) {
        MenuItems item = itemDAO.findById(itemId);
        if (item == null) {
            throw new NotFoundException(Config.RED + "Không tìm thấy món với ID: " + itemId + Config.RESET);
        }
        return item;
    }

    // Lấy toàn bộ menu
    public List<MenuItems> getAllItems() {
        return itemDAO.findAll();
    }

    // Lấy theo ID
    public MenuItems getItemById(String itemId) {
        return itemDAO.findById(itemId);
    }

}
