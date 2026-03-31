package presentation.admin;

import enums.ItemType;
import models.products.MenuItems;
import services.ItemService;
import utils.Config;
import utils.Input;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayItemMenu {
    public void displayItemsForAdmin() {
        ItemService itemService = new ItemService();
        List<MenuItems> currentList = new ArrayList<>();

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Tất cả");
            System.out.println("2. Đồ ăn");
            System.out.println("3. Đồ uống");
            System.out.println("0. Quay lại");
            System.out.print("Nhập lựa chọn: ");
            int subChoice = Input.inputIntegerPositive("");

            List<MenuItems> allItems = itemService.getAllItems();
            if (allItems == null) allItems = new ArrayList<>();

            switch (subChoice) {
                case 1 -> currentList = allItems;
                case 2 -> currentList = allItems.stream()
                        .filter(i -> i.getCategory() == ItemType.FOOD)
                        .collect(Collectors.toList());
                case 3 -> currentList = allItems.stream()
                        .filter(i -> i.getCategory() == ItemType.DRINK)
                        .collect(Collectors.toList());
                case 0 -> {
                    return;
                }
                default -> {
                    System.out.println(Config.RED + "Lựa chọn không hợp lệ!" + Config.RESET);
                    continue;
                }
            }

            if (currentList.isEmpty()) {
                System.out.println(Config.YELLOW + "Danh sách đang trống!" + Config.RESET);
            } else {
                List<String[]> rows = new ArrayList<>();
                for (MenuItems item : currentList) {
                    rows.add(new String[]{
                            item.getItemId(),
                            item.getItemName(),
                            String.format("%.2f", item.getPrice()),
                            item.getCategory().toString(),
                            item.isActive() ? "ACTIVE" : "INACTIVE"
                    });
                }
                TablePrinter.printTableWithPagination(
                        "MENU",
                        new String[]{"ITEM_ID", "ITEM_NAME", "PRICE", "CATEGORY", "IS_ACTIVE"},
                        rows,
                        5
                );
            }
        }
    }

    public List<MenuItems> displayItemsForCustomer() {
        ItemService itemService = new ItemService();

        System.out.println("\n=== MENU ===");
        System.out.println("1. Tất cả");
        System.out.println("2. Đồ ăn");
        System.out.println("3. Đồ uống");
        System.out.print("Nhập lựa chọn: ");
        int subChoice = Input.inputIntegerPositive("");

        List<MenuItems> allItems = itemService.getAllItems();
        if (allItems == null) allItems = new ArrayList<>();

        List<MenuItems> currentList;
        switch (subChoice) {
            case 1 -> currentList = allItems;
            case 2 -> currentList = allItems.stream()
                    .filter(i -> i.getCategory() == ItemType.FOOD)
                    .collect(Collectors.toList());
            case 3 -> currentList = allItems.stream()
                    .filter(i -> i.getCategory() == ItemType.DRINK)
                    .collect(Collectors.toList());
            default -> {
                System.out.println(Config.RED + "Lựa chọn không hợp lệ!" + Config.RESET);
                return new ArrayList<>();
            }
        }

        if (!currentList.isEmpty()) {
            List<String[]> rows = new ArrayList<>();
            for (MenuItems item : currentList) {
                rows.add(new String[]{
                        item.getItemId(),
                        item.getItemName(),
                        String.format("%.2f", item.getPrice()),
                        item.getCategory().toString()
                });
            }
            TablePrinter.printTableWithPagination(
                    "MENU",
                    new String[]{"ITEM_ID", "ITEM_NAME", "PRICE", "CATEGORY"},
                    rows,
                    5
            );
        }else {
            System.out.println(Config.YELLOW + "Danh sách đang trống!" + Config.RESET);
        }

        return currentList;
    }
}