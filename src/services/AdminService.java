package services;

import dao.*;
import enums.UserRole;
import exceptions.AdditionException;
import exceptions.DuplicateException;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import models.accounts.Staff;
import models.accounts.User;
import utils.Config;
import utils.DBConnection;
import utils.HashUtil;
import validation.ValidatePassword;
import validation.ValidatePhone;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminService {

    private UserDAO userDAO = new UserDAOImpl();
    private StaffDAO staffDAO = new StaffDAOImpl();
    private UserService userService = new UserService();
    private CustomerDAO customerDAO = new CustomerDAOImpl();

    // Xem danh sách staff
    public List<Staff> findAllStaff() {
        return staffDAO.findAll();
    }

    // Thêm Staff
    public void addStaff(String username, String password, String name, String phone) {
        Connection conn = DBConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            validateUsername(username);
            validatePassword(password);
            validatePhone(phone);

            String userId = userService.generateUserId();
            String hashPass = HashUtil.hashPassword(password);

            User user = new User(userId, username, hashPass, UserRole.STAFF);
            userDAO.insert(conn, user);

            String staffId = generateStaffId();

            String[] words = name.trim().toLowerCase().split("\\s+");
            StringBuilder result = new StringBuilder();
            for (String word : words) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
            String fullName = result.toString().trim();

            Staff staff = new Staff(staffId, userId, name, phone);
            staffDAO.insert(conn, staff);

            conn.commit();
            System.out.println(Config.GREEN_BRIGHT + "\nThêm staff thành công" + Config.RESET);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new AdditionException(Config.RED + "\nThêm staff thất bại" + Config.RESET);
        }
    }

    public String generateStaffId() {
        String lastId = staffDAO.getLastStaffId();

        if (lastId == null || lastId.isEmpty()) {
            return "ST001";
        }

        int lastNumber = Integer.parseInt(lastId.substring(2));
        return "ST" + String.format("%03d", lastNumber + 1);
    }

    // Các method validate
    public void validateUsername(String username) {
        if (userDAO.findByUsername(username) != null) {
            throw new DuplicateException(Config.RED + "Username đã tồn tại" + Config.RESET);
        }
    }

    public void validatePassword(String password) {
        if (!ValidatePassword.isValidatePassword(password)) {
            throw new ValidationException(Config.RED + "Mật khẩu phải có ít nhất 5 ký tự" + Config.RESET);
        }
    }

    public void validatePhone(String phone) {
        if (!ValidatePhone.isValidatePhone(phone)) {
            throw new ValidationException(Config.RED + "Số điện thoại không hợp lệ" + Config.RESET);
        }
    }

    // Xóa User
    public void deleteUser(String userId) {
        Connection conn = DBConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            User user = userDAO.findById(userId);
            if (user == null) {
                throw new NotFoundException("User không tồn tại");
            }

            if (user.getRole() == UserRole.STAFF) {
                staffDAO.deleteByUserId(conn, userId);
            } else if (user.getRole() == UserRole.CUSTOMER) {
                customerDAO.deleteByUserId(conn, userId);
            }

            userDAO.deleteById(conn, userId);

            conn.commit();
            System.out.println(Config.GREEN_BRIGHT + "\nXóa thành công!" + Config.RESET);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(Config.RED + "Xóa thất bại!" + Config.RESET);
        }
    }
}
