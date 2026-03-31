package presentation.admin;

import presentation.admin.user.UserMenu;
import utils.Config;
import utils.Input;

public class AdminMenu {
    public void showMenu() {
        while (true){
            System.out.println(Config.BLUE_BRIGHT + "\n===== ADMIN MENU =====" + Config.RESET);
            System.out.println("1. Quản lý User");
            System.out.println("2. Quản lý Máy trạm");
            System.out.println("3. Quản lý Món ăn");
            System.out.println("4. Quản lý Đơn hàng");
            System.out.println("5. Quản lý Đơn đặt máy");
            System.out.println("6. Quản lý Giao dịch");
            System.out.println("7. Báo cáo thống kê");
            System.out.println("0. Đăng xuất");
            System.out.print("Nhập lựa chọn: ");

            int choice = Input.inputIntegerPositive("");

            switch (choice) {
                case 1:
                    new UserMenu().showMenu();
                    break;
                case 2:
                    new ComputerMenu().showMenu();
                    break;
                case 3:
                    new ItemMenu().showMenu();
                    break;
                case 4:
                    new OrderMenu().showOrder();
                    break;
                case 5:
                    new BookingMenu().displayBooking();
                    break;
                case 6:
                    new TransactionMenu().showMenu();
                    break;
                case 7:
                    new ReportMenu().showMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Config.RED + "Lựa chọn không hợp lệ. Vui lòng nhập lại." + Config.RESET);
            }
        }
    }
}
