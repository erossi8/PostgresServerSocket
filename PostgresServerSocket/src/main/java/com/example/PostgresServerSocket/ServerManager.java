package com.example.PostgresServerSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The ServerManager class is responsible for managing the server's lifecycle and client connections.
 */
public class ServerManager {
    private static ExecutorService executor;
    private static ServerSocket serverSocket;

    /**
     * Starts the server on the specified IP address and port number, and begins accepting client connections.
     * @author Edoardo Rossi.
     * @param ipAddress The IP address to bind the server to.
     * @param PortNumber The port number to listen on.
     * @param url The JDBC URL for the database connection.
     * @param usr The username for database authentication.
     * @param pass The password for database authentication.
     * @throws IOException If an I/O error occurs while starting the server.
     */
    public static void executor(String ipAddress, int PortNumber, String url, String usr, String pass) {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                startServer(ipAddress, PortNumber, url, usr, pass);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void startServer(String ip, int port, String url, String usr, String pass) throws IOException {
        try {
            // Convert the IP address to InetAddress.
            String specificIpAddress = ip;
            InetAddress ipAddress = InetAddress.getByName(specificIpAddress);

            // Create a server socket and bind it to the specified IP and port.
            int serverPort = port;
            serverSocket = new ServerSocket(serverPort, 0, ipAddress);

            // Display server information.
            System.out.println("Server online on " + ipAddress + ":" + serverPort);

            // Create an executor for handling client connections.
            ExecutorService executor = Executors.newCachedThreadPool();

            // Continuously listen for and accept incoming client connections.
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from: " + clientSocket.getInetAddress());

                // Create a new ClientManager instance to handle the connection.
                ClientManager clientManager = new ClientManager(clientSocket, url, usr, pass);
                executor.submit(clientManager);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the server by closing the server socket and shutting down the executor.
     */
    public static void StopServer() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                System.out.println("Server offline");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}
