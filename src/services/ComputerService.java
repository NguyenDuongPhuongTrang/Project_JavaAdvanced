package services;

import dao.BookingDAOImpl;
import dao.ComputerDAO;
import dao.ComputerDAOImpl;
import enums.ComputerStatus;
import enums.ComputerType;
import exceptions.NotFoundException;
import models.computer.Computer;
import models.trans.Booking;
import utils.Config;

import java.time.LocalDateTime;
import java.util.List;

public class ComputerService {
    private ComputerDAO computerDAO = new ComputerDAOImpl();

    public void addComputer(String computerName, double price, ComputerType type) {
        String id = generateComputerId();
        Computer computer = new Computer(id, computerName, price, type, ComputerStatus.AVAILABLE, true);
        computerDAO.insert(computer);
        System.out.println(Config.GREEN_BRIGHT + "\nThêm máy thành công!" + Config.RESET);
    }

    public List<Computer> getAllComputers() {
        refreshComputerStatus();
        return computerDAO.findAll();
    }

    public List<Computer> getAvailableComputers() {
        return computerDAO.findAvailable();
    }

    public void updateComputer(String computerId, String computerName, ComputerType newType, Double newPrice, ComputerStatus newStatus) {
        Computer computer = findComputerById(computerId);

        if (computerName != null && !computerName.trim().isEmpty()) {
            computer.setName(computerName);
        }
        if (newType != null) {
            computer.setType(newType);
        }
        if (newPrice != null) {
            computer.setPrice(newPrice);
        }
        if (newStatus != null) {
            computer.setStatus(newStatus);
        }

        computerDAO.updateName(computerId, computer.getName());
        computerDAO.updateType(computerId, computer.getType());
        computerDAO.updatePrice(computerId, computer.getPrice());
        computerDAO.updateStatus(computerId, computer.getStatus());

        System.out.println(Config.GREEN_BRIGHT + "\nCập nhật máy thành công!" + Config.RESET);
    }

    public Computer findComputerById(String computerId) {
        Computer computer = computerDAO.findById(computerId);
        if (computer == null) {
            throw new NotFoundException(Config.RED + "Không tìm thấy máy với ID: " + computerId + Config.RESET);
        }
        return computer;
    }

    public void deleteComputer(String computerId) {
        findComputerById(computerId);
        computerDAO.delete(computerId);
        System.out.println(Config.GREEN_BRIGHT + "\nXóa máy thành công!" + Config.RESET);
    }

    public void refreshComputerStatus() {
        List<Computer> computers = computerDAO.findAll();
        List<Booking> bookings = new BookingDAOImpl().findAll();

        LocalDateTime now = LocalDateTime.now();

        for (Computer computer : computers) {
            boolean isUsing = false;

            for (Booking booking : bookings) {
                if (booking.getComputerId().equals(computer.getComputerId())
                        && booking.getStatus().equals("ACTIVE")
                        && now.isAfter(booking.getStartTime())
                        && now.isBefore(booking.getEndTime())) {

                    isUsing = true;
                    break;
                }
            }

            if (isUsing) {
                computerDAO.updateStatus(computer.getComputerId(), ComputerStatus.USING);
            } else {
                computerDAO.updateStatus(computer.getComputerId(), ComputerStatus.AVAILABLE);
            }
        }
    }

    //tìm booking ACTIVE của máy
    public Booking getCurrentBooking(String computerId) {
        List<Booking> bookings = new BookingDAOImpl().findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Booking b : bookings) {
            if (b.getComputerId().equals(computerId)
                    && b.getStatus().equals("ACTIVE")
                    && now.isAfter(b.getStartTime())
                    && now.isBefore(b.getEndTime())) {
                return b;
            }
        }
        return null;
    }

    // Tạo ID
    public String generateComputerId() {
        String lastId = computerDAO.getLastComputerId();
        if (lastId == null || lastId.isEmpty()) {
            return "PC001";
        }
        int lastNumber = Integer.parseInt(lastId.substring(2));
        return "PC" + String.format("%03d", lastNumber + 1);
    }
}