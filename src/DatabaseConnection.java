import java.sql.*;

/**
 *
 *
 * @author David Norman
 * @version Summer 2025
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/dungeondata?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1P0duser";

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found. Make sure MySQL Connector/J is in your classpath.", e);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database. Please check:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'dungeondata' exists");
            System.err.println("3. Username 'root' and password '1P0duser' are correct");
            System.err.println("4. MySQL is running on localhost:3306");
            throw e;
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}