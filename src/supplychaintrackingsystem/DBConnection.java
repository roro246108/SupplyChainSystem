package supplychaintrackingsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/supply_chain";
            String user = "root";
            String password = "";

            Connection conn = DriverManager.getConnection(url, user, password);

            System.out.println("Connected successfully!");
            return conn;

        } catch (Exception e) {
            try {
                return createDatabaseAndReconnect();
            } catch (Exception retryException) {
                retryException.printStackTrace();
                return null;
            }
        }
    }

    private static Connection createDatabaseAndReconnect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String serverUrl = "jdbc:mysql://192.168.1.185:3306/";
        String databaseName = "supply_chain";
        String user = "root";
        String password = "";

        try (Connection serverConn = DriverManager.getConnection(serverUrl, user, password);
             Statement stmt = serverConn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
        }

        return DriverManager.getConnection("jdbc:mysql://192.168.1.185:3306/" + databaseName, user, password);
    }

    public static void ensureUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(100) NOT NULL UNIQUE,
                    full_name VARCHAR(150) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    phone VARCHAR(20) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(50) NOT NULL,
                    active TINYINT(1) NOT NULL DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        try (Connection conn = connect()) {
            if (conn == null) {
                return;
            }

            try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
