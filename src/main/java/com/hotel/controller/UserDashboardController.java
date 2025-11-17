package com.hotel.controller;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation;
import com.hotel.model.User;
import com.hotel.session.Session;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

public class UserDashboardController {

    @FXML private ImageView profileImage;
    @FXML private Label loggedInUserLabel;

    @FXML private Label nextRoomLabel;
    @FXML private Label nextCheckInLabel;
    @FXML private Label nextStatusLabel;

    @FXML private TableView<Reservation> reservationTable;

    @FXML private TableColumn<Reservation, Integer> colId;
    @FXML private TableColumn<Reservation, Integer> colRoom;
    @FXML private TableColumn<Reservation, String> colType;
    @FXML private TableColumn<Reservation, java.sql.Date> colCheckIn;
    @FXML private TableColumn<Reservation, java.sql.Date> colCheckOut;
    @FXML private TableColumn<Reservation, String> colStatus;

    @FXML private javafx.scene.control.Button switchAccountButton;

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    private FilteredList<Reservation> filteredData;

    @FXML
    public void initialize() {

        User user = Session.getCurrentUser();

        /* LOAD PROFILE */
        loggedInUserLabel.setText(user.getUsername() + " (User)");

        try {
            Image img = new Image(getClass().getResourceAsStream("/images/user.png"));
            profileImage.setImage(img);
        } catch (Exception ignored) {}

        /* COLUMN SETUP */
        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        colRoom.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("roomNumber"));
        colType.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("roomType"));
        colCheckIn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("checkIn"));
        colCheckOut.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("checkOut"));
        colStatus.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));

        alignCenter(colId);
        alignCenter(colRoom);
        alignCenter(colType);
        alignCenter(colCheckIn);
        alignCenter(colCheckOut);
        alignCenter(colStatus);

        /* STATUS BADGES WITH ICONS */
        colStatus.setCellFactory(column -> new TableCell<>() {

            private final Label badge = new Label();

            {
                badge.setStyle("-fx-padding: 4 10; -fx-background-radius: 12; -fx-font-size: 12px;");
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }

                String icon = "";

                switch (status.toLowerCase()) {
                    case "confirmed":
                        icon = "✔️ ";
                        badge.getStyleClass().setAll("badge-confirmed");
                        break;
                    case "pending":
                        icon = "⏳ ";
                        badge.getStyleClass().setAll("badge-pending");
                        break;
                    case "checked-in":
                        icon = "🟢 ";
                        badge.getStyleClass().setAll("badge-checkedin");
                        break;
                    case "checked-out":
                        icon = "🏁 ";
                        badge.getStyleClass().setAll("badge-checkedout");
                        break;
                    case "cancelled":
                        icon = "❌ ";
                        badge.getStyleClass().setAll("badge-cancelled");
                        break;
                    case "no-show":
                        icon = "🚫 ";
                        badge.getStyleClass().setAll("badge-noshow");
                        break;
                    default:
                        icon = "• ";
                        badge.getStyleClass().setAll("badge-default");
                }

                badge.setText(icon + status);
                setGraphic(badge);
                setText(null);
            }
        });

        /* LOAD USER-ONLY RESERVATIONS */
        List<Reservation> list = reservationDAO.getReservationsByUserId(user.getId());
        reservations.setAll(list);

        filteredData = new FilteredList<>(reservations, r -> true);
        reservationTable.setItems(filteredData);

        /* SET NEXT UPCOMING STAY */
        populateNextStay();

        /* SWITCH ACCOUNT */
        switchAccountButton.setOnAction(e -> switchAccount());
    }

    /* ALIGN CENTER HELPER */
    private void alignCenter(TableColumn<?, ?> col) {
        col.setStyle("-fx-alignment: CENTER;");
    }

    /* UPCOMING STAY LOGIC */
    private void populateNextStay() {
        List<Reservation> sorted = reservations.stream()
                .filter(r -> r.getCheckIn().after(new java.util.Date()))
                .sorted(Comparator.comparing(Reservation::getCheckIn))
                .toList();

        if (sorted.isEmpty()) {
            nextRoomLabel.setText("No upcoming stays");
            nextCheckInLabel.setText("");
            nextStatusLabel.setText("");
            return;
        }

        Reservation next = sorted.get(0);

        nextRoomLabel.setText("Room " + next.getRoomNumber() + " – " + next.getRoomType());
        nextCheckInLabel.setText("Check-in: " + next.getCheckIn());
        nextStatusLabel.setText("Status: " + next.getStatus());
    }

    /* SWITCH ACCOUNT LOGIC */
    private void switchAccount() {
        Session.clear();
        try {
            FXMLLoader fx = new FXMLLoader(getClass().getResource("/fxml/LoginScreen.fxml"));
            Parent root = fx.load();

            Stage st = (Stage) switchAccountButton.getScene().getWindow();
            Scene sc = new Scene(root);
            sc.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            st.setScene(sc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
