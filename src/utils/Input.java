package utils;

import java.util.Scanner;

public class Input {
    private static final Scanner sc = new Scanner(System.in);

    public static String input(String title) {
        System.out.print(title);
        return sc.nextLine();
    }

    public static String inputString(String title) {
        while (true) {
            System.out.print(title);
            String input = sc.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println(Config.RED + "Không được để trống" + Config.RESET);
                System.out.print("Nhập lại: ");
            }else {
                return input;
            }
        }
    }

    public static int inputIntegerPositive(String title) {
        while (true) {
            System.out.print(title);
            try {
                int input = Integer.parseInt(sc.nextLine());
                if (input < 0) {
                    System.out.println(Config.RED + "Phải nhập số lớn hơn không!" + Config.RESET);
                    System.out.print("Nhập lại: ");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println(Config.RED + "Nhập không hợp lệ!" + Config.RESET);
                System.out.print("Nhập lại: ");
            }
        }
    }

    public static double inputDoublePositive(String title) {
        while (true) {
            System.out.print(title);
            try {
                double input = Double.parseDouble(sc.nextLine());
                if (input < 0) {
                    System.out.println(Config.RED + "Phải nhập số lớn hơn không!" + Config.RESET);
                    System.out.print("Nhập lại: ");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println(Config.RED + "Nhập không hợp lệ!" + Config.RESET);
                System.out.print("Nhập lại: ");
            }
        }
    }

    public static Double inputDoubleUpdate(String title) {
        while (true) {
            System.out.print(title);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                return null;
            }
            try {
                double input = Double.parseDouble(line);
                if (input < 0) {
                    System.out.println(Config.RED + "Phải nhập số lớn hơn hoặc bằng 0!" + Config.RESET);
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println(Config.RED + "Nhập không hợp lệ!" + Config.RESET);
                System.out.print("Nhập lại: ");
            }
        }
    }
}
