package presentation;

import observer.TransactionLogger;
import observer.TransactionSubject;
import presentation.auth.LoginUI;
import presentation.auth.RegisterUI;
import utils.Config;
import utils.DBConnection;
import utils.DatabaseInitializer;
import utils.Input;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer initializer = new DatabaseInitializer();
        initializer.init();
        TransactionSubject subject = TransactionSubject.getInstance();
        subject.addObserver(new TransactionLogger());

        LoginUI loginUI = new LoginUI();
        RegisterUI registerUI = new RegisterUI();
        int choice;
        do {
            System.out.println(Config.YELLOW_BOLD + "\n=== CYBER GAME ===" + Config.RESET);
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