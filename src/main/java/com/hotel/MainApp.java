package com.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML from resources/fxml/LoginScreen.fxml
        try (InputStream fxmlStream = getClass().getResourceAsStream("/fxml/LoginScreen.fxml")) {
            if (fxmlStream == null) {
                throw new RuntimeException("LoginScreen.fxml not found in resources/fxml/");
            }
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginScreen.fxml"));
            Scene scene = new Scene(root, 520, 320);
            primaryStage.setTitle("Hotel Reservation System - Login");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(420);
            primaryStage.setMinHeight(260);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
