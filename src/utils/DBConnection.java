package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private static Connection connection = null;

    public static Connection getConnection(){
        try {
            String url = "jdbc:mysql://localhost:3306/cyber_game_db";
            String user = "root";
            String password = "061006";

            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DBConnection getInstance(){
        if (connection == null){
            connection = getConnection();
        }
        return instance;
    }

}
