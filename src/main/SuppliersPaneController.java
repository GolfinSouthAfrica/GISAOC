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

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class SuppliersPaneController implements Initializable{

    @FXML private TextField searchTxf;
    @FXML private ScrollPane suppliersScrollPane;
    @FXML private VBox suppliersList;
    @FXML private ComboBox sortBy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sortBy.getItems().clear();
        sortBy.getItems().addAll("Unsorted", "Name-Ascend", "Name-Descend", "Province-Ascend", "Province-Descend", "Category-Ascend", "Category-Descend");
        sortBy.getSelectionModel().select(0);
        sortBy.valueProperty().addListener((obs, oldItem, newItem) -> {
            populateSuppliers(sortBy.getSelectionModel().getSelectedItem().toString());
        });
        populateSuppliers(sortBy.getSelectionModel().getSelectedItem().toString());
        Main.connectionHandler.suppliers.addListener((InvalidationListener) e -> {
            populateSuppliers("");
            sortBy.getSelectionModel().select(0);
        });
    }

    private void populateSuppliers(String sort){
        if(!Main.connectionHandler.packages.isEmpty()) {
            ObservableList<HBox> supplierCards = FXCollections.observableArrayList();
            List<Supplier> temp = Main.connectionHandler.suppliers;
            if(temp.size() > 1) {
                if (sort.matches("Name-Ascend")) {
                    temp.sort(Comparator.comparing(Supplier::getSupplierName));
                } else if (sort.matches("Name-Descend")) {
                    temp.sort(Comparator.comparing(Supplier::getSupplierName).reversed());
                } else if (sort.matches("Province-Ascend")) {
                    temp.sort(Comparator.comparing(Supplier::getSupplierName));
                    temp.sort(Comparator.comparing(Supplier::getProvince));
                } else if (sort.matches("Province-Descend")) {
                    temp.sort(Comparator.comparing(Supplier::getSupplierName).reversed());
                    temp.sort(Comparator.comparing(Supplier::getProvince).reversed());
                } else if (sort.matches("Category-Ascend")) {
                    temp.sort(Comparator.comparing(Supplier::getSupplierName));
                    temp.sort(Comparator.comparing(Supplier::getProvince));
                    temp.sort(Comparator.comparing(Supplier::getCategory));
                } else if (sort.matches("Category-Descend")) {
                    temp.sort(Comparator.comparing(Supplier::getSupplierName).reversed());
                    temp.sort(Comparator.comparing(Supplier::getProvince).reversed());
                    temp.sort(Comparator.comparing(Supplier::getCategory).reversed());
                }
            }
            for (Supplier s : Main.connectionHandler.suppliers) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("SuppliersCardPane.fxml"));
                HBox root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SuppliersCardPaneController scc = loader.getController();
                scc.initData(s);
                supplierCards.add(root);
            }
            Platform.runLater(() -> {
                suppliersList.getChildren().clear();
                suppliersList.getChildren().addAll(supplierCards);
            });
        } else {
            Platform.runLater(() -> suppliersList.getChildren().clear());
        }
    }

    public void searchButtonClick(){
        ObservableList<Supplier> displayList = FXCollections.observableArrayList();
        if (!searchTxf.getText().matches("")) {
            for (Supplier s: Main.connectionHandler.suppliers) {
                if (s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase())||s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase())||s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase())||s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                    displayList.add(s);
                }
            }
        } else {
            displayList.addAll(Main.connectionHandler.suppliers);
        }
        ObservableList<HBox> supplierCards = FXCollections.observableArrayList();
        for (Supplier s: displayList) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SuppliersCardPane.fxml"));
            HBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SuppliersCardPaneController scc = loader.getController();
            scc.initData(s);
            supplierCards.add(root);
        }
        suppliersList.getChildren().clear();
        suppliersList.getChildren().addAll(supplierCards);
    }

    public void addButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewSupplierPane.fxml"));
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
