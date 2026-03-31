package presentation.admin;

import enums.ItemType;
import exceptions.NotFoundException;
import models.products.MenuItems;
import services.ItemService;
import utils.Config;
import utils.Input;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;

public class ItemMenu {
    private ItemService itemService = new ItemService();

    public void showMenu() {
        while (true) {
            System.out.println(Config.BLUE_BRIGHT + "\n==== QUẢN LÝ MENU F&B ====" + Config.RESET);
            System.out.println("1. Xem MENU");
            System.out.println("2. Thêm món");
            System.out.println("3. Sửa món");
            System.out.println("4. Xoá món");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            int choice = Input.inputIntegerPositive("");

            switch (choice) {
                case 1:
                    DisplayItemMenu displayItemMenu = new DisplayItemMenu();
                    displayItemMenu.displayItemsForAdmin();
                    break;
                case 2:
                    addItem();
                    break;
                case 3:
                    updateItem();
                    break;
                case 4:
                    deleteItem();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Config.RED + "Lựa chọn không hợp lệ. Vui lòng nhập lại." + Config.RESET);
            }
        }
    }

    // Thêm
    private void addItem() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== THÊM MÓN ===" + Config.RESET);

        System.out.print("Tên món: ");
        String name = Input.inputString("");

        System.out.print("Giá: ");
        double price = Input.inputDoublePositive("");

        System.out.println("Loại: ");
        System.out.println("1. FOOD");
        System.out.println("2. DRINK");
        System.out.print("Chọn: ");
        int choiceCategory = Input.inputIntegerPositive("");
        ItemType category = choiceCategory == 1 ? ItemType.FOOD : ItemType.DRINK;

        itemService.addItem(name, price, category);
    }

    // Cập nhật
    private void updateItem() {
        List<MenuItems> list = itemService.getAllItems();
        if (list == null || list.isEmpty()) {
            System.out.println(Config.YELLOW + "\nDanh sách món ăn đang trống!" + Config.RESET);
            return;
        }

        System.out.println(Config.BLUE_BRIGHT + "\n=== CẬP NHẬT MÓN ===" + Config.RESET);
        while (true) {
            System.out.print("ID món cần cập nhật: ");
            String id = Input.inputString("");

            try {
                MenuItems item = itemService.findItemById(id);

                System.out.print("Tên mới (Enter để bỏ qua): ");
                String name = Input.input("");

                System.out.print("Giá mới (0 để bỏ qua): ");
                Double price = Input.inputDoubleUpdate("");

                ItemType category = null;

                while (true) {
                    System.out.println("Loại hiện tại: " + item.getCategory());
                    System.out.println("1. FOOD");
                    System.out.println("2. DRINK");
                    System.out.println("0. Không thay đổi");
                    System.out.print("Chọn: ");

                    int choice = Input.inputIntegerPositive("");

                    if (choice == 1) {
                        category = ItemType.FOOD;
                        break;
                    } else if (choice == 2) {
                        category = ItemType.DRINK;
                        break;
                    } else if (choice == 0) {
                        category = null;
                        break;
                    } else {
                        System.out.println(Config.RED + "Lựa chọn không hợp lệ!" + Config.RESET);
                    }
                }

                itemService.updateItem(id, name, price, category);

                break;
            } catch (NotFoundException e) {
                System.out.println("\n" + e.getMessage());
            }
        }
    }

    // Xóa
    private void deleteItem() {
        List<MenuItems> list = itemService.getAllItems();
        if (list == null || list.isEmpty()) {
            System.out.println(Config.YELLOW + "\nDanh sách món ăn đang trống!" + Config.RESET);
            return;
        }
        System.out.println(Config.BLUE_BRIGHT + "\n=== XÓA MÓN ===" + Config.RESET);
        System.out.print("ID món cần xóa: ");
        String id = Input.inputString("");

        try {
            itemService.deleteItem(id);
        } catch (NotFoundException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
