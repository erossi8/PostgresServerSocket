package com.example.PostgresServerSocket;
import java.io.*;
import java.net.Socket;
import java.sql.*;
/**
 * This class, ClientManager, manages incoming client connections and interacts with a PostgreSQL database.
 * It extends the Thread class to handle multiple clients concurrently.
 */
class ClientManager extends Thread {

    private Socket clientSocket;
    private Connection dbConnection;

    /**
     * Constructs a new ClientManager instance.
     * @author Edoardo Rossi.
     * @param clientSocket The client socket representing the connected client.
     * @param dbURL        The URL of the PostgreSQL database.
     * @param username     The username for database authentication.
     * @param password     The password for database authentication.
     */
    public ClientManager(Socket clientSocket, String dbURL, String username, String password) {
        this.clientSocket = clientSocket;
        try {
            // Establish a database connection using the provided URL, username, and password.
            dbConnection = DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The run method is executed when the thread is started. It handles client requests
     * by executing SQL queries against the connected database.
     */
    public void run() {
        try {
            // Initialize input and output streams for communication with the client.
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String query;

            // Process incoming queries from the client.
            while ((query = in.readLine()) != null) {
                try {
                    // Create a statement, execute the query, and retrieve results.
                    Statement stmt = dbConnection.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    // Build the header row with column names.
                    StringBuilder header = new StringBuilder();
                    for (int i = 1; i <= columnCount; i++) {
                        header.append(rsmd.getColumnName(i));
                        if (i < columnCount) {
                            header.append("\t");
                        }
                    }
                    out.println(header);

                    // Send query results to the client.
                    while (rs.next()) {
                        StringBuilder row = new StringBuilder();
                        for (int i = 1; i <= columnCount; i++) {
                            row.append(rs.getString(i));
                            if (i < columnCount) {
                                row.append("\t"); // Use the appropriate separator
                            }
                        }
                        out.println(row);
                    }

                    // Close resources.
                    rs.close();
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Close input, output, and client socket.
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the database connection if it's still open.
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
