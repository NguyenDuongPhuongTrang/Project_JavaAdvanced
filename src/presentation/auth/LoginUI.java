package presentation.auth;

import enums.UserRole;
import exceptions.AuthenticationException;
import models.accounts.Customer;
import models.accounts.User;
import presentation.CustomerMenu;
import presentation.StaffMenu;
import presentation.admin.AdminMenu;
import services.CustomerService;
import services.UserService;
import utils.Config;
import utils.Input;

public class LoginUI {
    private UserService userService = new UserService();
    private CustomerService customerService = new CustomerService();

    public void login() {
        while (true) {
            System.out.println(Config.BLUE_BRIGHT + "\n=== ĐĂNG NHẬP ===" + Config.RESET);

            System.out.print("Username: ");
            String username = Input.inputString("");

            while (true) {
                System.out.print("Mật khẩu (Phải có ít nhất 5 ký tự): ");
                String password = Input.inputString("");

                try {
                    User user = userService.login(username, password);

                    System.out.println(Config.GREEN_BRIGHT +
                            "\nĐăng nhập thành công! Chào mừng " + user.getUsername()
                            + Config.RESET);

                    switch (user.getRole()) {
                        case ADMIN:
                            new AdminMenu().showMenu();
                            break;
                        case STAFF:
                            new StaffMenu().showMenu();
                            break;
                        case CUSTOMER:
                            Customer customer = customerService.getByUserId(user.getUserId());
                            CustomerMenu customerMenu = new CustomerMenu(customer);
                            customerMenu.showMenu();
                            break;
                    }
                    return;

                } catch (AuthenticationException e) {

                    String error = e.getMessage();

                    if (error.contains("User không tồn tại")) {
                        System.out.println(Config.RED + error + Config.RESET);

                        String choice;
                        while (true) {
                            System.out.println(Config.BLUE_BRIGHT +
                                    "\nBạn chưa có tài khoản? Chọn:\n" +
                                    "[0] Đăng nhập lại\n" +
                                    "[1] Đăng ký" + Config.RESET);

                            System.out.print("Nhập lựa chọn: ");
                            choice = Input.inputString("");

                            if (choice.equals("0") || choice.equals("1")) break;
                            else System.out.println(Config.RED +
                                    "Lựa chọn không hợp lệ. Vui lòng nhập 0 hoặc 1." + Config.RESET);
                        }

                        if (choice.equals("1")) {
                            new RegisterUI().register();
                            return;
                        }

                        break;

                    }
                    else {
                        System.out.println(Config.RED +
                                "\nSai mật khẩu! Vui lòng nhập lại.\n"
                                + Config.RESET);
                    }
                }
            }
        }
    }
}