package com.example.PostgresServerSocket;
import java.sql.*;

/**
 * The DatabaseManager class provides a method for checking the existence of a PostgreSQL database.
 * It extends HelloController, which suggests some form of relationship with a web controller.
 */
public class DatabaseManager extends HelloController {

    /**
     * Checks if a database with the given name exists in the specified PostgreSQL server.
     * @author Edoardo Rossi.
     * @param url      The URL of the PostgreSQL server.
     * @param user     The username for database authentication.
     * @param password The password for database authentication.
     * @param dbName   The name of the database to check for existence.
     * @return True if the database exists, false otherwise.
     */
    public static boolean CheckDatabase(String url, String user, String password, String dbName) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String checkDbQuery = "SELECT datname FROM pg_catalog.pg_database WHERE datname = '" + dbName + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(checkDbQuery);

            // If a row is returned, the database exists.
            if (resultSet.next()) {
                System.out.println("The database " + dbName + " exists.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error during database connection: " + e.getMessage());
        }
        return false;
    }
}
