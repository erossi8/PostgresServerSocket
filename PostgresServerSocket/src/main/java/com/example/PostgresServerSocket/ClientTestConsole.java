package com.example.PostgresServerSocket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
/**
 * This class, ClientTestConsole, represents a simple client application that connects to a server
 * and sends SQL queries to it, then displays the query results in the console.
 * @author Edoardo Rossi.
 */
public class ClientTestConsole {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String serverAddress = "localhost"; // The address of the server to connect to.
        int serverPort = 5000; // The port number to use for the server connection.

        try {
            // Establish a socket connection to the server.
            Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Prompt the user to input a query.
            System.out.println("Insert a query: ");
            String query = scanner.nextLine();

            // Send the query to the server.
            out.println(query);

            String line;
            // Read and display the query results received from the server.
            while (!(line = in.readLine()).equals("")) {
                System.out.println(line);
            }

            // Close input, output, and the socket.
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
