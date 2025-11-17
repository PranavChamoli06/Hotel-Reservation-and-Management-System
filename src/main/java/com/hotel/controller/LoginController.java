package com.hotel.controller;

import com.hotel.dao.UserDAO;
import com.hotel.model.User;
import com.hotel.util.PasswordUtils;
import com.hotel.session.Session;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        errorLabel.setText("");
        loginButton.setOnAction(evt -> attemptLogin());
        passwordField.setOnAction(evt -> attemptLogin());
    }

    private void attemptLogin() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        loginButton.setDisable(true);
        errorLabel.setText("Authenticating...");

        Task<Boolean> task = new Task<>() {
            private User authenticatedUser = null;
            @Override
            protected Boolean call() {
                try {
                    Optional<User> userOpt = userDAO.getUserByUsername(username);
                    if (userOpt.isEmpty()) return false;
                    User user = userOpt.get();
                    boolean verified = PasswordUtils.verifyPassword(password, user.getSalt(), user.getPasswordHash());
                    if (verified) {
                        authenticatedUser = user;
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                    updateMessage("ERROR:" + ex.getMessage());
                    return false;
                }
            }

            @Override
            protected void succeeded() {
                loginButton.setDisable(false);
                String msg = getMessage();
                if (msg != null && msg.startsWith("ERROR:")) {
                    showError("Unable to authenticate. Try again later.");
                    return;
                }
                boolean success = getValue();
                if (success && authenticatedUser != null) {
                    onLoginSuccess(authenticatedUser);
                } else {
                    showError("Invalid username or password.");
                }
            }

            @Override
            protected void failed() {
                loginButton.setDisable(false);
                showError("Authentication failed (internal error).");
                if (getException() != null) getException().printStackTrace();
            }
        };

        task.messageProperty().addListener((obs, old, nw) -> {
            if (nw != null && nw.startsWith("ERROR:")) Platform.runLater(() -> showError("Unable to reach authentication server."));
        });

        new Thread(task).start();
    }

    private void showError(String text) {
        Platform.runLater(() -> {
            errorLabel.setText(text);
            loginButton.setDisable(false);
        });
    }

    private void onLoginSuccess(User user) {
        Platform.runLater(() -> {
            try {
                // store user
                Session.setCurrentUser(user);

                // update login activity (writes last_login and IP)
                try {
                    String ip = getLocalIp();
                    userDAO.updateLoginActivity(user.getId(), ip);
                    user.setLastLoginNow();
                    user.setLastLoginIp(ip);
                } catch (Exception ex) {
                    // non-fatal
                    ex.printStackTrace();
                }

                // Load Dashboard
                String fxml;

                switch (user.getRole().toLowerCase()) {
                    case "admin":
                    case "manager":
                    case "staff":
                        fxml = "/fxml/Dashboard.fxml";
                        break;
                    case "user":
                        fxml = "/fxml/UserDashboard.fxml";
                        break;
                    default:
                        fxml = "/fxml/Dashboard.fxml";
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene sc = new Scene(root, 900, 600);
                String css = getClass().getResource("/css/style.css").toExternalForm();
                sc.getStylesheets().add(css);
                stage.setTitle("Hotel Reservation System - Dashboard");
                stage.setScene(sc);
                stage.centerOnScreen();
            } catch (Exception ex) {
                ex.printStackTrace();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                VBox root = new VBox(12);
                root.setStyle("-fx-padding:20;");
                Label welcome = new Label("Welcome, " + user.getUsername());
                welcome.setStyle("-fx-font-size:16px; -fx-font-weight:600;");
                Label role = new Label("Role: " + user.getRole());
                root.getChildren().addAll(welcome, role);
                stage.setScene(new Scene(root, 420, 200));
                stage.centerOnScreen();
            }
        });
    }

    private String getLocalIp() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
