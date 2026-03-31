package presentation.admin.user;

import models.accounts.Customer;
import models.accounts.Staff;
import models.accounts.User;
import services.AdminService;
import services.CustomerService;
import services.StaffService;
import services.UserService;
import utils.Config;
import utils.Input;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;

public class DisplayUserMenu {
    public void displayUsers() {
        UserService userService = new UserService();
        CustomerService customerService = new CustomerService();
        AdminService adminService = new AdminService();
        while (true){
            System.out.println(Config.BLUE_BRIGHT + "\n=== LỰA CHỌN DANH SÁCH USER ===" + Config.RESET);
            System.out.println("1. Xem tất cả user");
            System.out.println("2. Xem tất cả customer");
            System.out.println("3. Xem tất cả staff");
            System.out.println("0. Quay lại");
            System.out.print("Nhập lựa chọn: ");

            int subChoice = Input.inputIntegerPositive("");

            switch (subChoice){
                case 1:
                    List<String[]> rows = new ArrayList<>();
                    List<User> users = userService.getAllUsers();

                    if (users == null || users.isEmpty()) {
                        System.out.println(Config.YELLOW + "\nDanh sách user đang trống!" + Config.RESET);
                        break;
                    }

                    for (User u : users) {
                        rows.add(new String[]{
                                u.getUserId(),
                                u.getUsername(),
                                u.getRole().toString(),
                        });
                    }

                    TablePrinter.printTableWithPagination(
                            "DANH SÁCH USER",
                            new String[]{"ID", "USERNAME", "ROLE"},
                            rows,
                            5
                    );

                    break;
                case 2:
                    List<String[]> rowCus = new ArrayList<>();
                    List<Customer> customers = customerService.findAll();

                    if (customers == null || customers.isEmpty()) {
                        System.out.println(Config.YELLOW + "\nDanh sách customer đang trống!" + Config.RESET);
                        break;
                    }

                    for (Customer c : customers) {
                        rowCus.add(new String[]{
                                c.getCustomerId(),
                                c.getUserId(),
                                c.getName(),
                                String.valueOf(c.getBalance())
                        });
                    }

                    TablePrinter.printTableWithPagination(
                            "DANH SÁCH CUSTOMER",
                            new String[]{"CUSTOMER_ID", "USER_ID", "NAME", "BALANCE"},
                            rowCus,
                            5
                    );

                    break;
                case 3:
                    List<String[]> rowSt = new ArrayList<>();
                    List<Staff> staffs = adminService.findAllStaff();

                    if (staffs == null || staffs.isEmpty()) {
                        System.out.println(Config.YELLOW + "\nDanh sách staff đang trống!" + Config.RESET);
                        break;
                    }

                    for (Staff s : staffs) {
                        rowSt.add(new String[]{
                                s.getStaffId(),
                                s.getUserId(),
                                s.getName(),
                                s.getPhone()
                        });
                    }

                    TablePrinter.printTableWithPagination(
                            "DANH SÁCH STAFF",
                            new String[]{"STAFF_ID", "USER_ID", "NAME", "PHONE"},
                            rowSt,
                            5
                    );
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Config.RED + "\nLựa chọn không hợp lệ. Vui lòng nhập lại." + Config.RESET);
            }

        }
    }
}
