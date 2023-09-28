module com.example.emotionalsongs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.PostgresServerSocket to javafx.fxml;
    exports com.example.PostgresServerSocket;
}