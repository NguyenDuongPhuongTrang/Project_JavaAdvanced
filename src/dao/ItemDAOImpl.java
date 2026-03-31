package dao;

import enums.ItemType;
import models.products.MenuItems;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    Connection conn = DBConnection.getConnection();

    // Lấy tất cả món
    @Override
    public List<MenuItems> findAll() {
        List<MenuItems> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MenuItems item = new MenuItems(
                        rs.getString("itemId"),
                        rs.getString("itemName"),
                        rs.getDouble("price"),
                        ItemType.valueOf(rs.getString("category")),
                        rs.getBoolean("is_active")
                );
                list.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm theo ID
    @Override
    public MenuItems findById(String itemId) {
        String sql = "SELECT * FROM menu_items WHERE itemId=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new MenuItems(
                        rs.getString("itemId"),
                        rs.getString("itemName"),
                        rs.getDouble("price"),
                        ItemType.valueOf(rs.getString("category")),
                        rs.getBoolean("is_active")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm món
    @Override
    public void insert(MenuItems item) {
        String sql = "INSERT INTO menu_items(itemId, itemName, price, category, is_active) VALUES(?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemId());
            ps.setString(2, item.getItemName());
            ps.setDouble(3, item.getPrice());
            ps.setString(4, item.getCategory().name());
            ps.setBoolean(5, true);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật món
    @Override
    public void update(MenuItems item) {
        String sql = "UPDATE menu_items SET itemName=?, price=?, category=? WHERE itemId=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getCategory().name());
            ps.setString(4, item.getItemId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa món
    @Override
    public void delete(String itemId) {
        String sql = "UPDATE menu_items SET is_active = false WHERE itemId=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy ID cuối cùng
    @Override
    public String getLastItemId() {
        String sql = "SELECT itemId FROM menu_items ORDER BY itemId DESC LIMIT 1";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString("itemId");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
