package main;

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
    @FXML ComboBox quantityCmb;
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
        provinceCmb.getItems().addAll("All", "Western Cape", "Eastern Cape", "Northern Cape", "Gauteng", "Kwa-zulu Natal", "North West", "Mpumulanga", "Limpopo", "Free-State", "South Africa", "World");
        sortByCmb.getItems().clear();
        sortByCmb.getItems().addAll("Name", "Province");
        quantityCmb.getItems().clear();
        for (int i = 0; i < 500; i++) {
            quantityCmb.getItems().add(i);
        }
    }

    public void searchButtonClick(){
        ObservableList displayList = FXCollections.observableArrayList();
        if (!searchTxf.getText().matches("")) {
            for (TripPackage p: Main.connectionHandler.packages) {
                if (p.getPackageName().contains(searchTxf.getText())||p.getCategory().contains(searchTxf.getText())||p.getProvince().contains(searchTxf.getText())) {
                    displayList.add(p);
                }
            }
        } else {
            displayList.addAll(Main.connectionHandler.packages);
        }
        packagesListView.setItems(displayList);
    }

    public TripPackage getPackage(){//TODO
        if(packagesListView.getSelectionModel().getSelectedItem() != null){
            return (TripPackage) packagesListView.getSelectionModel().getSelectedItem();
        } else {
            return null;
        }

    }

}

