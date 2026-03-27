package presentation;

import exceptions.AuthenticationException;
import models.accounts.User;
import services.UserService;
import utils.Input;

public class RegisterUI {
    private UserService userService = new UserService();

    public void register(){
        while (true){
            System.out.println("\n=== ĐĂNG KÝ ===");
            System.out.println("Username: ");
            String username = Input.inputString("");
            System.out.println("Password: ");
            String password = Input.inputString("");
            try {
                userService.register(username, password);
                System.out.println("Đăng ký thành công!");
                break;
            } catch (AuthenticationException e){
                System.out.println(e.getMessage());
                String choice;
                while (true) {
                    System.out.println("Bạn đã có tài khoản? Chọn [0] Đăng ký lại, [1] Đăng nhập");
                    choice = Input.inputString("");

                    if (choice.equals("0") || choice.equals("1")) {
                        break;
                    } else {
                        System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập 0 hoặc 1.");
                    }
                }

                if (choice.equals("1")) {
                    new LoginUI().login();
                    break;
                }
            }
        }
    };
}
