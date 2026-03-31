package presentation.admin;

import enums.ComputerStatus;
import enums.ComputerType;
import exceptions.NotFoundException;
import models.computer.Computer;
import models.trans.Booking;
import services.ComputerService;
import utils.Config;
import utils.Input;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;

public class ComputerMenu {
    private ComputerService computerService = new ComputerService();

    public void showMenu() {
        while (true) {
            System.out.println(Config.BLUE_BRIGHT + "\n===== QUẢN LÝ MÁY TRẠM =====" + Config.RESET);
            System.out.println("1. Hiển thị danh sách máy trạm");
            System.out.println("2. Thêm máy trạm mới");
            System.out.println("3. Cập nhật thông tin máy trạm");
            System.out.println("4. Xóa máy trạm");
            System.out.println("0. Quay lại");
            System.out.print("Nhập lựa chọn: ");

            int choice = Input.inputIntegerPositive("");

            switch (choice) {
                case 1:
                    displayComputer();
                    break;
                case 2:
                    addComputer();
                    break;
                case 3:
                    updateComputer();
                    break;
                case 4:
                    deleteComputer();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Config.RED + "Lựa chọn không hợp lệ. Vui lòng nhập lại." + Config.RESET);
            }
        }
    }


    // Hiển thị
    private void displayComputer() {
        List<Computer> computersToDisplay = computerService.getAllComputers();
        List<String[]> rows = new ArrayList<>();

        if (computersToDisplay == null || computersToDisplay.isEmpty()) {
            System.out.println(Config.YELLOW + "\nDanh sách PC đang trống!" + Config.RESET);
            return;
        }

        for (Computer c : computersToDisplay) {
            Booking current = computerService.getCurrentBooking(c.getComputerId());

            String user = "-";
            String time = "-";

            if (current != null) {
                user = current.getCustomerId();
                time = current.getStartTime().toLocalTime() + " - " + current.getEndTime().toLocalTime();
            }

            rows.add(new String[]{
                    c.getComputerId(),
                    c.getName(),
                    String.valueOf(c.getPrice()),
                    c.getType().toString(),
                    c.getStatus().toString(),
                    user,
                    time
            });
        }

        TablePrinter.printTableWithPagination(
                "DANH SÁCH PC",
                new String[]{
                        "ID", "NAME", "PRICE", "TYPE", "STATUS", "USER", "TIME"
                },
                rows,
                5
        );
    }

    // Thêm
    private void addComputer() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== THÊM MÁY TRẠM ===" + Config.RESET);
        System.out.print("Tên máy: ");
        String name = Input.inputString("");
        System.out.print("Loại máy:\n");
        System.out.println("1. STANDARD\n2. VIP\n3. STREAM");
        System.out.print("Chọn: ");
        int typeChoice = Input.inputIntegerPositive("");
        ComputerType type = ComputerType.STANDARD;
        switch (typeChoice) {
            case 1:
                type = ComputerType.STANDARD;
                break;
            case 2:
                type = ComputerType.VIP;
                break;
            case 3:
                type = ComputerType.STREAM;
                break;
            default:
                System.out.println(Config.RED + "\nLựa chọn loại máy không hợp lệ. Vui lòng nhập lại." + Config.RESET);
                break;
        }
        System.out.print("Giá/giờ: ");
        double price = Input.inputDoublePositive("");
        computerService.addComputer(name, price, type);
    }

    // Cập nhật
    private void updateComputer() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== CẬP NHẬT MÁY TRẠM ===" + Config.RESET);
        List<Computer> computersToUpdate = computerService.getAllComputers();
        if (computersToUpdate == null || computersToUpdate.isEmpty()) {
            System.out.println(Config.YELLOW + "\nDanh sách PC đang trống!" + Config.RESET);
            return;
        }

        while (true) {
            System.out.print("ID máy cần cập nhật: ");
            String id = Input.inputString("");
            try {
                Computer c = computerService.findComputerById(id);
                System.out.print("Tên máy (Enter để bỏ qua): ");
                String nameUpdate = Input.input("");
                ComputerType typeUpdate = null;

                while (true) {
                    System.out.println("Loại máy:");
                    System.out.println("1. STANDARD\n2. VIP\n3. STREAM\n0. Không thay đổi");
                    System.out.print("Chọn: ");
                    int updateTypeChoice = Input.inputIntegerPositive("");
                    switch (updateTypeChoice) {
                        case 1 -> {
                            typeUpdate = ComputerType.STANDARD;
                            break;
                        }
                        case 2 -> {
                            typeUpdate = ComputerType.VIP;
                            break;
                        }
                        case 3 -> {
                            typeUpdate = ComputerType.STREAM;
                            break;
                        }
                        case 0 -> {
                            typeUpdate = null;
                            break;
                        }
                        default -> {
                            System.out.println(Config.RED + "Lựa chọn không hợp lệ. Nhập lại!" + Config.RESET);
                            continue;
                        }
                    }
                    break;
                }
                System.out.print("Giá/giờ (Enter để bỏ qua): ");
                Double priceUpdate = Input.inputDoubleUpdate("");
                System.out.print("Tình trạng máy:\n");
                ComputerStatus statusUpdate = null;
                while (true) {
                    System.out.println("1. AVAILABLE\n2. USING\n3. MAINTENANCE\n0. Không thay đổi");
                    System.out.print("Chọn: ");
                    int statusChoice = Input.inputIntegerPositive("");
                    switch (statusChoice) {
                        case 1 -> {
                            statusUpdate = ComputerStatus.AVAILABLE;
                            break;
                        }
                        case 2 -> {
                            statusUpdate = ComputerStatus.USING;
                            break;
                        }
                        case 3 -> {
                            statusUpdate = ComputerStatus.MAINTENANCE;
                            break;
                        }
                        case 0 -> {
                            statusUpdate = null;
                            break;
                        }
                        default -> {
                            System.out.println(Config.RED + "Lựa chọn không hợp lệ. Nhập lại!" + Config.RESET);
                            continue;
                        }
                    }
                    break;
                }
                computerService.updateComputer(id, nameUpdate, typeUpdate, priceUpdate, statusUpdate);
                break;
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Xóa
    private void deleteComputer() {
        System.out.println(Config.BLUE_BRIGHT + "\n=== XÓA MÁY TRẠM ===" + Config.RESET);
        List<Computer> computersToDelete = computerService.getAllComputers();
        if (computersToDelete == null || computersToDelete.isEmpty()) {
            System.out.println(Config.YELLOW + "\nDanh sách PC đang trống!" + Config.RESET);
            return;
        }

        System.out.print("ID máy cần xóa: ");
        String idDelete = Input.inputString("");

        try {
            computerService.deleteComputer(idDelete);
        } catch (NotFoundException e) {
            System.out.println(Config.RED + e.getMessage() + Config.RESET);
        }
    }

}
