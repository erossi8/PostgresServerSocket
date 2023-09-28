package com.example.PostgresServerSocket;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The HelloController class is responsible for managing the user interface and handling user interactions.
 * It implements the Initializable interface for initialization purposes.
 * @author Edoardo Rossi.
 */
public class HelloController implements Initializable {

    // FXML-defined GUI elements
    @FXML
    public TextField GetIpAddress;
    @FXML
    public TextField GetPort;
    @FXML
    public TextField GetJdbcUrl;
    @FXML
    public TextField GetPostgresUsername;
    @FXML
    public TextField GetPostgresPassword;
    @FXML
    public Label LogLabel;
    @FXML
    public Button StartButton;
    @FXML
    public Button StopButton;
    @FXML
    public ImageView img;
    @FXML
    public ProgressBar progress;
    @FXML
    public CheckBox CBsave;
    @FXML
    public TextField dbName;
    @FXML
    public ProgressIndicator progress2;

    /**
     * Starts the socket server and initializes the database if needed.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void StartSocketServer() throws IOException {
        // Extract user inputs
        String ipAddress = GetIpAddress.getText();
        String PortNumber = GetPort.getText();
        String url = GetJdbcUrl.getText() + "/" + dbName.getText();
        String usr = GetPostgresUsername.getText();
        String pass = GetPostgresPassword.getText();
        String dbname = dbName.getText();

        // Check if the database exists
        boolean db = DatabaseManager.CheckDatabase(url, usr, pass, dbname);

        if ((ipAddress.length() == 0) || (PortNumber.length() == 0) || (url.length() == 19) || (usr.length() == 0) || (pass.length() == 0)) {
            LogLabel.setText("Error");
        } else {
            if (CBsave.isSelected()) {
                // Save configuration to a file
                FileWriter fw = new FileWriter("init.txt");
                String info = ipAddress + "\n" + PortNumber + "\n" + GetJdbcUrl.getText() + "\n" + usr + "\n" + pass + "\n" + dbname;
                fw.write(info);
                fw.close();
            }
            if (db) {
                // Stop the server if it's running and start a new instance
                ServerManager.StopServer();
                ServerManager.executor(ipAddress, Integer.parseInt(PortNumber), url, usr, pass);
                LogLabel.setText("Server online on IP address: " + ipAddress + " and port: " + PortNumber);
            } else {
                LogLabel.setText(dbname + " database does not exist");
            }
        }
    }

    /**
     * Stops the server.
     */
    public void StopServer() {
        ServerManager.StopServer();
        LogLabel.setText("Server offline");
    }

    /**
     * Initializes the controller with default values from a configuration file.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load configuration from a file
            BufferedReader br = new BufferedReader(new FileReader("init.txt"));
            GetIpAddress.setText(br.readLine());
            GetPort.setText(br.readLine());
            GetJdbcUrl.setText(br.readLine());
            GetPostgresUsername.setText(br.readLine());
            GetPostgresPassword.setText(br.readLine());
            dbName.setText(br.readLine());
        } catch (IOException e) {
            // Handle exceptions if the configuration file is not found
        }
    }
}
