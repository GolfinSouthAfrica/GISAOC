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
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class PackagesPaneController implements Initializable {

    @FXML private TextField searchTxf;
    @FXML private ScrollPane packagesScrollPane;
    @FXML private VBox packagesList;
    @FXML private ComboBox sortBy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sortBy.getItems().clear();
        sortBy.getItems().addAll("Unsorted", "Name-Ascend", "Name-Descend", "Province-Ascend", "Province-Descend", "Category-Ascend", "Category-Descend");
        sortBy.getSelectionModel().select(0);
        sortBy.valueProperty().addListener((obs, oldItem, newItem) -> {
            populatePackages(sortBy.getSelectionModel().getSelectedItem().toString());
        });
        populatePackages(sortBy.getSelectionModel().getSelectedItem().toString());
        Main.connectionHandler.packages.addListener((InvalidationListener) e -> {
            populatePackages("");
            sortBy.getSelectionModel().select(0);
        });
    }

    private void populatePackages(String sort){
        if(!Main.connectionHandler.packages.isEmpty()) {
            ObservableList<HBox> packageCards = FXCollections.observableArrayList();
            List<TripPackage> temp = Main.connectionHandler.packages;
            if(temp.size() > 1) {
                if (sort.matches("Name-Ascend")) {
                    temp.sort(Comparator.comparing(TripPackage::getPackageName));
                } else if (sort.matches("Name-Descend")) {
                    temp.sort(Comparator.comparing(TripPackage::getPackageName).reversed());
                } else if (sort.matches("Province-Ascend")) {
                    temp.sort(Comparator.comparing(TripPackage::getPackageName));
                    temp.sort(Comparator.comparing(TripPackage::getProvince));
                } else if (sort.matches("Province-Descend")) {
                    temp.sort(Comparator.comparing(TripPackage::getPackageName).reversed());
                    temp.sort(Comparator.comparing(TripPackage::getProvince).reversed());
                } else if (sort.matches("Category-Ascend")) {
                    temp.sort(Comparator.comparing(TripPackage::getPackageName));
                    temp.sort(Comparator.comparing(TripPackage::getProvince));
                    temp.sort(Comparator.comparing(TripPackage::getCategory));
                } else if (sort.matches("Category-Descend")) {
                    temp.sort(Comparator.comparing(TripPackage::getPackageName).reversed());
                    temp.sort(Comparator.comparing(TripPackage::getProvince).reversed());
                    temp.sort(Comparator.comparing(TripPackage::getCategory).reversed());
                }
            }
            for (TripPackage tp : Main.connectionHandler.packages) {
                if (!tp.getPackageName().contains("Bespoke")) {
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
        } else {
            Platform.runLater(() -> packagesList.getChildren().clear());
        }
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
