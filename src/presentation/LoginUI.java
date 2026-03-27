package presentation;

import exceptions.AuthenticationException;
import models.accounts.User;
import services.UserService;
import utils.Input;

public class LoginUI {
    private UserService userService = new UserService();

    public void login(){
        while (true){
            System.out.println("\n=== ĐĂNG NHẬP ===");
            System.out.print("Username: ");
            String username = Input.inputString("");
            System.out.print("Password: ");
            String password = Input.inputString("");
            try {
                User user = userService.login(username, password);
                System.out.println("Đăng nhập thành công! Chào mừng " + user.getUsername());
                switch (user.getRole()){
                    case ADMIN:
                        new AdminMenu().showMenu();
                        break;
                    case STAFF:
                        new StaffMenu().showMenu();
                        break;
                    case CUSTOMER:
                        new CustomerMenu().showMenu();
                        break;
                }
                break;
            } catch (AuthenticationException e){
                System.out.println(e.getMessage());
                String choice;
                while (true) {
                    System.out.println("Bạn chưa có tài khoản? Chọn: " + "\n" +
                            "[0] Đăng nhập lại" + "\n" +
                            "[1] Đăng ký");
                    choice = Input.inputString("");

                    if (choice.equals("0") || choice.equals("1")) {
                        break;
                    } else {
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập 0 hoặc 1.");
                    }
                }

                if (choice.equals("1")) {
                    new RegisterUI().register();
                    break;
                }
            }
        }
    };
}
