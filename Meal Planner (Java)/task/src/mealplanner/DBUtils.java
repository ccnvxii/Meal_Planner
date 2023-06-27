package mealplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/meals_db";
    private static final String USER = "postgres";
    private static final String PASS = "1111";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println(e.getMessage() + "\n");
        }
        return connection;
    }
}