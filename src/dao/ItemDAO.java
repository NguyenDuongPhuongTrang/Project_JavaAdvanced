package dao;

import models.products.MenuItems;

import java.util.List;

public interface ItemDAO {
    List<MenuItems> findAll();
    MenuItems findById(String itemId);
    void insert(MenuItems item);
    void update(MenuItems item);
    void delete(String itemId);
    String getLastItemId();
}
