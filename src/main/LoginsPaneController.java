package main;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Booking;
import models.Login;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginsPaneController implements Initializable {

    @FXML TextField searchTxf;
    @FXML ScrollPane loginsScrollPane;
    @FXML VBox loginsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.connectionHandler.logins.addListener((InvalidationListener) e -> {
            populateLogins();
        });
        populateLogins();
    }

    private void populateLogins(){
        ObservableList<HBox> loginsCards = FXCollections.observableArrayList();
        for (Login l: Main.connectionHandler.logins) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LoginsCardPane.fxml"));
            HBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LoginsCardPaneController lcpc = loader.getController();
            lcpc.initData(l);
            loginsCards.add(root);
        }
        Platform.runLater(() -> {
            loginsList.getChildren().clear();
            loginsList.getChildren().addAll(loginsCards);
        });
    }

    public void initData(){

    }

    public void searchButtonClick(){
        ObservableList<Login> displayList = FXCollections.observableArrayList();
        if (!searchTxf.getText().matches("")) {
            for (Login ln: Main.connectionHandler.logins) {
                if (ln.getLoginName().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                    displayList.add(ln);
                }
            }
        } else {
            displayList.addAll(Main.connectionHandler.logins);
        }
        ObservableList<HBox> loginsCards = FXCollections.observableArrayList();
        for (Login l: displayList) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LoginsCardPane.fxml"));
            HBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LoginsCardPaneController lcpc = loader.getController();
            lcpc.initData(l);
            loginsCards.add(root);
        }
        Platform.runLater(() -> {
            loginsList.getChildren().clear();
            loginsList.getChildren().addAll(loginsCards);
        });
    }

    public void addLoginButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewLoginPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
