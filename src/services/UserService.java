package services;

import dao.UserDAO;
import dao.UserDAOImpl;
import enums.UserRole;
import exceptions.AuthenticationException;
import exceptions.DuplicateException;
import models.accounts.User;
import utils.HashUtil;

public class UserService {
    private UserDAO userDAO = new UserDAOImpl();

    public User login(String username, String password){
        User user = userDAO.findByUsername(username);
        if (user == null){
            throw new AuthenticationException("User không tồn tại");
        }
        boolean isValid = HashUtil.verifyPassword(password, user.getPassword());
        if (!isValid) {
            throw new AuthenticationException("Mật khẩu không chính xác");
        }
        return user;
    }

    public void register(String username, String password){
        if (userDAO.findByUsername(username) != null){
            throw new DuplicateException("Username đã tồn tại");
        }
        String userId = generateId("CUSTOMER");
        String hashPass = HashUtil.hashPassword(password);
        UserRole role = UserRole.CUSTOMER;
        User user = new User(userId, username, hashPass, role);
        userDAO.insert(user);
    }

    public String generateId(String role){
        String begin = getBeginChar(role);
        String lastId = userDAO.getLastUserIdByRole(begin);
        if (lastId == null) {
            return begin + "001";
        }
        int lastNumber = Integer.parseInt(lastId.substring(2));
            return begin + String.format("%03d", lastNumber + 1);
    }

    private String getBeginChar(String role) {
        switch (role) {
            case "STAFF":
                return "ST";
            case "CUSTOMER":
                return "CM";
            default:
                throw new IllegalArgumentException("Role không hợp lệ");
        }
    }
}
