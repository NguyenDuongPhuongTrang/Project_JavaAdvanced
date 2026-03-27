package presentation;

import utils.DBConnection;
import utils.DatabaseInitializer;
import utils.Input;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnection.getInstance().getConnection();
        DatabaseInitializer initializer = new DatabaseInitializer();
        initializer.init();

        LoginUI loginUI = new LoginUI();
        RegisterUI registerUI = new RegisterUI();
        int choice;
        do {
            System.out.println("=== CYBER GAME ===");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("0. Thoát");

            choice = Input.inputIntegerPositive("Nhập lựa chọn: ");
            switch (choice){
                case 1:
                    loginUI.login();
                    break;
                case 2:
                    registerUI.register();
                    break;
                case 0:
                    System.exit(0);
            }
        } while (choice != 0);
    }
}