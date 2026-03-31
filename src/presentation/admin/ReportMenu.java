package presentation.admin;

import services.ReportService;
import utils.Config;
import utils.Input;

import java.time.LocalDate;

public class ReportMenu {
    public void showMenu() {
        ReportService reportService = new ReportService();
        while (true){
            System.out.println(Config.BLUE_BRIGHT + "\n=== BÁO CÁO THỐNG KÊ ===" + Config.RESET);
            System.out.println("1. Báo cáo doanh thu theo ngày");
            System.out.println("2. Báo cáo doanh thu theo tháng");
            System.out.println("0. Thoát");
            System.out.print("\nNhập lựa chọn: ");
            int choice = Input.inputIntegerPositive("");

            switch (choice) {
                case 1:
                    System.out.print("Nhập ngày (yyyy-MM-dd): ");
                    String input = Input.inputString("");
                    LocalDate date = LocalDate.parse(input);

                    double revenueByDate = reportService.getRevenueByDate(date);

                    System.out.printf("\nDoanh thu ngày %s: %.2f\n", date, revenueByDate);
                    break;
                case 2:
                    System.out.print("Nhập tháng: ");
                    int month = Input.inputIntegerPositive("");

                    System.out.print("Nhập năm: ");
                    int year = Input.inputIntegerPositive("");

                    double revenueByMonth = reportService.getRevenueByMonth(month, year);

                    System.out.printf("\nDoanh thu tháng %d/%d: %.2f\n", month, year, revenueByMonth);
                    break;
                case 0:
                    return;
            }
        }

    }
}
