import java.sql.*;

/**
 * Singleton class for managing database connections.
 * Provides a single point of access to the MySQL database.
 *
 * @author David Norman
 * @author Georgia Karwhite
 * @version Summer 2025
 */
public class DatabaseConnection {
    private static DatabaseConnection myInstance;
    private Connection myConnection;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DATABASENAME?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    /**
     * Private constructor to establish database connection.
     *
     * @throws SQLException if database connection fails
     */
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            myConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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

    /**
     * Gets the singleton instance of DatabaseConnection.
     *
     * @return the DatabaseConnection instance
     * @throws SQLException if database connection fails
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (myInstance == null || myInstance.getConnection().isClosed()) {
            myInstance = new DatabaseConnection();
        }
        return myInstance;
    }

    /**
     * Gets the database connection.
     *
     * @return the Connection object
     */
    public Connection getConnection() {
        return myConnection;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (myConnection != null && !myConnection.isClosed()) {
                myConnection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}