package presentation.auth;

import exceptions.DuplicateException;
import exceptions.ValidationException;
import services.UserService;
import utils.Config;
import utils.Input;

public class RegisterUI {

    private UserService userService = new UserService();

    public void register() {

        while (true) {
            System.out.println(Config.BLUE_BRIGHT + "\n=== ĐĂNG KÝ ===" + Config.RESET);

            // ===== USERNAME =====
            String username;
            while (true) {
                try {
                    System.out.print("Username: ");
                    username = Input.inputString("");
                    userService.validateUsername(username);
                    break;

                } catch (DuplicateException e) {
                    System.out.println("\n" + Config.RED + e.getMessage() + Config.RESET);

                    String choice;
                    while (true) {
                        System.out.println(Config.BLUE_BRIGHT +
                                "\nBạn đã có tài khoản?" +
                                "\n[0] Nhập username khác" +
                                "\n[1] Đăng nhập" +
                                Config.RESET);

                        System.out.print("Nhập lựa chọn: ");
                        choice = Input.inputString("");

                        if (choice.equals("0") || choice.equals("1")) break;

                        System.out.println(Config.RED + "\nLựa chọn không hợp lệ. Vui lòng nhập 0 hoặc 1." + Config.RESET);
                    }

                    if (choice.equals("1")) {
                        new LoginUI().login();
                        return;
                    }

                } catch (ValidationException e) {
                    System.out.println(Config.RED + e.getMessage() + Config.RESET);
                }
            }

            String password;
            while (true) {
                try {
                    System.out.print("Mật khẩu (Phải có ít nhất 5 ký tự): ");
                    password = Input.inputString("");
                    userService.validatePassword(password);
                    break;

                } catch (ValidationException e) {
                    System.out.println(Config.RED + e.getMessage() + Config.RESET);
                }
            }

            System.out.print("Tên: ");
            String name = Input.inputString("");
            userService.register(username, password, name);
            System.out.println(Config.GREEN_BRIGHT + "\nĐăng ký thành công!" + Config.RESET);
            return;


        }
    }
}