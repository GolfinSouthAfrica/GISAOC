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
import java.util.ResourceBundle;

public class SuppliersPaneController implements Initializable{

    @FXML private TextField searchTxf;
    @FXML private ScrollPane suppliersScrollPane;
    @FXML private VBox suppliersList;
    @FXML private ComboBox sortBy;//TODO

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.connectionHandler.suppliers.addListener((InvalidationListener) e -> {
            populateSuppliers();
        });
        populateSuppliers();
    }

    private void populateSuppliers(){
        ObservableList<HBox> supplierCards = FXCollections.observableArrayList();
        for (Supplier s: Main.connectionHandler.suppliers) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SuppliersCardPane.fxml"));
            HBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            root.setPrefWidth(suppliersScrollPane.getPrefWidth() - 10);
            SuppliersCardPaneController scc = loader.getController();
            scc.initData(s, suppliersScrollPane.getPrefWidth() / 3);
            supplierCards.add(root);
        }
        Platform.runLater(() -> {
            suppliersList.getChildren().clear();
            suppliersList.getChildren().addAll(supplierCards);
        });
    }

    public void searchButtonClick(){
        ObservableList<Supplier> displayList = FXCollections.observableArrayList();
        if (!searchTxf.getText().matches("")) {
            for (Supplier s: Main.connectionHandler.suppliers) {
                if (s.getSupplierName().contains(searchTxf.getText())||s.getContactNumber().contains(searchTxf.getText())||s.getProvince().contains(searchTxf.getText())||s.getEmail().contains(searchTxf.getText())) {
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
            root.setPrefWidth(suppliersScrollPane.getPrefWidth() - 10);
            SuppliersCardPaneController scc = loader.getController();
            scc.initData(s, suppliersScrollPane.getPrefWidth() / 3);
            supplierCards.add(root);
        }
        suppliersList.getChildren().clear();
        suppliersList.getChildren().addAll(supplierCards);
    }

    public void addButtonClick(){//TODO add
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddSupplierPane.fxml"));
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
