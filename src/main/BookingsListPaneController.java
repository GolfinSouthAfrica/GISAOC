package main;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Booking;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class BookingsListPaneController implements Initializable {

    @FXML TextField searchTxf;
    @FXML ScrollPane bookingsScrollPane;
    @FXML VBox bookingsList;
    @FXML ComboBox sortBy;
    private String process;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sortBy.getItems().clear();
        sortBy.getItems().addAll("Unsorted", "GSNumber-Ascend", "GSNumber-Descend", "ClientName-Ascend", "ClientName-Descend", "Arrival-Ascend", "Arrival-Descend");
        sortBy.getSelectionModel().select(0);
        sortBy.valueProperty().addListener((obs, oldItem, newItem) -> {
            populateBookings(sortBy.getSelectionModel().getSelectedItem().toString());
        });
    }

    private void populateBookings(String sort){
        if(!Main.connectionHandler.bookings.isEmpty()) {
            ObservableList<HBox> bookingCards = FXCollections.observableArrayList();
            List<Booking> temp = Main.connectionHandler.bookings;
            if(temp.size() > 1) {
                if (sort.matches("GSNumber-Ascend")) {
                    temp.sort(Comparator.comparing(Booking::getGsNumber));
                } else if (sort.matches("GSNumber-Descend")) {
                    temp.sort(Comparator.comparing(Booking::getGsNumber).reversed());
                } else if (sort.matches("ClientName-Ascend")) {
                    temp.sort(Comparator.comparing(Booking::getGsNumber));
                    temp.sort(Comparator.comparing(Booking::getClientName));
                } else if (sort.matches("ClientName-Descend")) {
                    temp.sort(Comparator.comparing(Booking::getGsNumber).reversed());
                    temp.sort(Comparator.comparing(Booking::getClientName).reversed());
                } else if (sort.matches("Arrival-Ascend")) {
                    temp.sort(Comparator.comparing(Booking::getGsNumber));
                    temp.sort(Comparator.comparing(Booking::getClientName));
                    temp.sort(Comparator.comparing(Booking::getArrival));
                } else if (sort.matches("Arrival-Descend")) {
                    temp.sort(Comparator.comparing(Booking::getGsNumber).reversed());
                    temp.sort(Comparator.comparing(Booking::getClientName).reversed());
                    temp.sort(Comparator.comparing(Booking::getArrival).reversed());
                }
            }
            for (Booking b : Main.connectionHandler.bookings) {
                if (b.getProcess().contains(process)) {
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
                    scc.initData(b, process);
                    bookingCards.add(root);
                }
            }
            Platform.runLater(() -> {
                bookingsList.getChildren().clear();
                bookingsList.getChildren().addAll(bookingCards);
            });
        } else {
            Platform.runLater(() -> bookingsList.getChildren().clear());
        }
    }

    public void initData(String process, String search){
        this.process = process;
        populateBookings(sortBy.getSelectionModel().getSelectedItem().toString());
        Main.connectionHandler.bookings.addListener((InvalidationListener) e -> {
            populateBookings("");
            sortBy.getSelectionModel().select(0);
        });
        if(!search.matches("")) {
            searchTxf.setText(search);
            searchButtonClick();
        }
    }

    public void searchButtonClick(){
        ObservableList<Booking> displayList = FXCollections.observableArrayList();
        if (!searchTxf.getText().matches("")) {
            for (Booking bk: Main.connectionHandler.bookings) {
                if (bk.getGsNumber().toLowerCase().contains(searchTxf.getText().toLowerCase())||bk.getClientName().toLowerCase().contains(searchTxf.getText().toLowerCase())||bk.getConsultant().toLowerCase().toLowerCase().contains(searchTxf.getText())||bk.getContactNumber().toLowerCase().contains(searchTxf.getText().toLowerCase())||bk.getEmail().toLowerCase().contains(searchTxf.getText().toLowerCase())||bk.getArrival().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                    displayList.add(bk);
                }
            }
        } else {
            displayList.addAll(Main.connectionHandler.bookings);
        }
        ObservableList<HBox> bookingCards = FXCollections.observableArrayList();
        for (Booking b: displayList) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("BookingsCardPane.fxml"));
            HBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BookingsCardPaneController scc = loader.getController();
            scc.initData(b, process);
            bookingCards.add(root);
        }
        Platform.runLater(() -> {
            bookingsList.getChildren().clear();
            bookingsList.getChildren().addAll(bookingCards);
        });
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
        loader.setLocation(getClass().getResource("BookingsPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
