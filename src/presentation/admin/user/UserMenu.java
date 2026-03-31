package presentation.admin.user;

import exceptions.DuplicateException;
import exceptions.ValidationException;
import services.AdminService;
import services.UserService;
import utils.Config;
import utils.Input;

public class UserMenu {
    private UserService userService = new UserService();
    private AdminService adminService = new AdminService();

    public void showMenu() {
        while (true) {
            System.out.println(Config.BLUE_BRIGHT + "\n==== QUẢN LÝ USER ====" + Config.RESET);
            System.out.println("1. Xem danh sách user");
            System.out.println("2. Thêm staff");
            System.out.println("3. Thêm customer");
            System.out.println("4. Xoá user");
            System.out.println("0. Quay lại");
            System.out.print("Nhập lựa chọn: ");

            int choice = Input.inputIntegerPositive("");

            switch (choice) {
                case 1:
                    DisplayUserMenu displayUserMenu = new DisplayUserMenu();
                    displayUserMenu.displayUsers();
                    break;
                case 2:
                    addStaff();
                    break;
                case 3:
                    addCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Config.RED + "Lựa chọn không hợp lệ. Vui lòng nhập lại." + Config.RESET);
            }
        }
    }

    // Thêm staff
    private void addStaff() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== THÊM STAFF ===" + Config.RESET);
        String usernameStaff;
        while (true) {
            try {
                System.out.print("Username: ");
                usernameStaff = Input.inputString("");
                adminService.validateUsername(usernameStaff);
                break;
            } catch (DuplicateException e) {
                String error = e.getMessage();
                System.out.print(Config.RED + error + Config.RESET);
                System.out.println(Config.RED + ". Vui lòng nhập lại." + Config.RESET);
            }
        }

        String passwordStaff;
        while (true) {
            try {
                System.out.print("Mật khẩu (Phải có ít nhất 5 ký tự): ");
                passwordStaff = Input.inputString("");
                adminService.validatePassword(passwordStaff);
                break;
            } catch (ValidationException e) {
                String error = e.getMessage();
                System.out.print(Config.RED + error + Config.RESET);
                System.out.println(Config.RED + ". Vui lòng nhập lại." + Config.RESET);
            }
        }

        System.out.print("Tên: ");
        String nameStaff = Input.inputString("");

        String phone;
        while (true) {
            try {
                System.out.print("Số điện thoại: ");
                phone = Input.inputString("");
                adminService.validatePhone(phone);
                break;
            } catch (ValidationException e) {
                String error = e.getMessage();
                System.out.print(Config.RED + error + Config.RESET);
                System.out.println(Config.RED + ". Vui lòng nhập lại." + Config.RESET);
            }
        }

        adminService.addStaff(usernameStaff, passwordStaff, nameStaff, phone);
    }

    // Thêm customer
    private void addCustomer() {
        System.out.println( Config.BLUE_BRIGHT + "\n=== THÊM CUSTOMER ===" + Config.RESET);

        String usernameCustomer;
        while (true) {
            try {
                System.out.print("Username: ");
                usernameCustomer = Input.inputString("");
                userService.validateUsername(usernameCustomer);
                break;
            } catch (DuplicateException e) {
                String error = e.getMessage();
                System.out.print(Config.RED + error + Config.RESET);
                System.out.println(Config.RED + ". Vui lòng nhập lại." + Config.RESET);
            }
        }

        String passwordCustomer;
        while (true) {
            try {
                System.out.print("Mật khẩu (Phải có ít nhất 5 ký tự): ");
                passwordCustomer = Input.inputString("");
                userService.validatePassword(passwordCustomer);
                break;
            } catch (ValidationException e) {
                String error = e.getMessage();
                System.out.print(Config.RED + error + Config.RESET);
                System.out.println(Config.RED + ". Vui lòng nhập lại." + Config.RESET);
            }
        }

        System.out.print("Tên: ");
        String nameCustomer = Input.inputString("");
        userService.register(usernameCustomer, passwordCustomer, nameCustomer);
        System.out.println(Config.GREEN_BRIGHT + "\nThêm khách hàng thành công!" + Config.RESET);
    }

    // Xóa
    private void deleteCustomer() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== XÓA USER ===" + Config.RESET);
        System.out.print("ID user muốn xóa: ");
        String userId = Input.inputString("");
        adminService.deleteUser(userId);
    }
}
