package com.example.PostgresServerSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;

/**
 * The HelloApplication class serves as the entry point for a JavaFX-based application.
 * It initializes the graphical user interface (GUI) and handles the application's lifecycle.
 */
public class HelloApplication extends Application {

    /**
     * This method is called when the application is launched.
     * @author Edoardo Rossi.
     * @param stage The primary stage representing the application's main window.
     * @throws IOException If there is an error while loading the FXML resource.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ServerStart.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 435, 512);

        // Configure the primary stage.
        stage.setResizable(false);
        stage.setOnCloseRequest((WindowEvent event) -> {
            ServerManager.StopServer();
        });
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method is the entry point of the application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch();
    }
}
