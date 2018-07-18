package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Product;
import models.TripPackage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewQuotePackagePaneController implements Initializable{

    @FXML ListView packagesListView;
    @FXML ComboBox provinceCmb;
    @FXML ComboBox sortByCmb;
    @FXML TextField searchTxf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        packagesListView.getItems().clear();
        packagesListView.getItems().addAll(Main.connectionHandler.packages);
        populateComboBoxes();
    }

    public void initData(){
        populateComboBoxes();
    }

    public void populateComboBoxes(){
        provinceCmb.getItems().clear();
        provinceCmb.getItems().addAll("All", "Western Cape", "Eastern Cape", "Northern Cape", "Gauteng", "Kwa-zulu Natal", "North West", "Mpumalanga", "Limpopo", "Free-State", "South Africa", "World");
        provinceCmb.getSelectionModel().select("All");
        sortByCmb.getItems().clear();
        sortByCmb.getItems().addAll("Name", "Province");
        sortByCmb.getSelectionModel().select("Name");

    }

    public void searchButtonClick(){
        ObservableList displayList = FXCollections.observableArrayList();
        if (!searchTxf.getText().matches("")) {
            for (TripPackage p: Main.connectionHandler.packages) {
                if (p.getPackageName().toLowerCase().contains(searchTxf.getText().toLowerCase())||p.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase())||p.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                    displayList.add(p);
                }
            }
        } else {
            displayList.addAll(Main.connectionHandler.packages);
        }
        Platform.runLater(() -> {
            packagesListView.setItems(displayList);
        });
    }

    public TripPackage getPackage(){
        if(packagesListView.getSelectionModel().getSelectedItem() != null){
            return (TripPackage) packagesListView.getSelectionModel().getSelectedItem();
        } else {
            return null;
        }

    }

}

