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
import models.Supplier;
import models.TripPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PackagesPaneController implements Initializable {

    @FXML private TextField searchTxf;
    @FXML private ScrollPane packagesScrollPane;
    @FXML private VBox packagesList;
    @FXML private ComboBox sortBy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.connectionHandler.packages.addListener((InvalidationListener) e -> {
            populatePackages();
        });
        populatePackages();
        sortBy.getItems().clear();
        sortBy.getItems().addAll("Name", "Province");
        sortBy.getSelectionModel().select(0);
    }

    private void populatePackages(){
        ObservableList<HBox> packageCards = FXCollections.observableArrayList();
        for (TripPackage tp: Main.connectionHandler.packages) {
            if(!tp.getPackageName().contains("Bespoke")) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("PackagesCardPane.fxml"));
                HBox root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PackagesCardPaneController pcpc = loader.getController();
                pcpc.initData(tp);
                packageCards.add(root);
            }
        }
        Platform.runLater(() -> {
            packagesList.getChildren().clear();
            packagesList.getChildren().addAll(packageCards);
        });
    }

    public void searchButtonClick(){
        ObservableList<TripPackage> displayList = FXCollections.observableArrayList();
        if (!searchTxf.getText().matches("")) {
            for (TripPackage tp: Main.connectionHandler.packages) {
                if (tp.getPackageName().toLowerCase().contains(searchTxf.getText().toLowerCase())||tp.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase())||tp.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                    displayList.add(tp);
                }
            }
        } else {
            displayList.addAll(Main.connectionHandler.packages);
        }
        ObservableList<HBox> packagesCards = FXCollections.observableArrayList();
        for (TripPackage tp: displayList) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PackagesCardPane.fxml"));
            HBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PackagesCardPaneController pcpc = loader.getController();
            pcpc.initData(tp);
            packagesCards.add(root);
        }
        packagesList.getChildren().clear();
        packagesList.getChildren().addAll(packagesCards);
    }

    public void addButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewPackagePane.fxml"));
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
