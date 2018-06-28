package main;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Booking;
import models.Supplier;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookingsListPaneController implements Initializable {

    @FXML TextField searchTxf;
    @FXML ComboBox sortByCmb;
    @FXML ScrollPane bookingsScrollPane;
    @FXML VBox bookingsList;
    private String process;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.connectionHandler.bookings.addListener((InvalidationListener) e -> {
            populateBookings();
        });
        populateBookings();
    }

    private void populateBookings(){
        ObservableList<HBox> bookingCards = FXCollections.observableArrayList();
        for (Booking b: Main.connectionHandler.bookings) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("BookingsCardPane.fxml"));
            HBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            root.setPrefWidth(bookingsScrollPane.getPrefWidth() - 10);
            BookingsCardPaneController scc = loader.getController();
            scc.initData(b);
            bookingCards.add(root);
        }
        Platform.runLater(() -> {
            bookingsList.getChildren().clear();
            bookingsList.getChildren().addAll(bookingCards);
        });
    }

    public void initData(String process){
        this.process = process;
    }

    public void searchButtonClick(){//TODO

    }

    public void newQuoteButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuotePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewQuotePaneController nqpc = loader.getController();
        nqpc.initNoMailData("BookingsListPane", process);
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
