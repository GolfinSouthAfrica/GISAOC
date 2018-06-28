package main;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.ContactDetails;
import models.Supplier;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewSuppliersPaneController implements Initializable {

    @FXML Label supplierNameTxf;
    @FXML Label provinceTxf;
    @FXML Label categoryTxf;
    @FXML Label addressTxf;
    @FXML Label coOrdinatesTxf;
    @FXML ScrollPane contactsScrollPane;
    @FXML VBox contactsList;
    private Supplier supplier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Supplier supplier){
        Main.connectionHandler.suppliers.addListener((InvalidationListener) e -> {
            this.supplier = supplier;
            supplierNameTxf.setText(supplier.getSupplierName());
            categoryTxf.setText(supplier.getCategory());
            provinceTxf.setText(supplier.getProvince());
            addressTxf.setText(supplier.getAddress());
            coOrdinatesTxf.setText(supplier.getCoOrdinates());
            populateContacts();
        });
        this.supplier = supplier;
        supplierNameTxf.setText(supplier.getSupplierName());
        categoryTxf.setText(supplier.getCategory());
        provinceTxf.setText(supplier.getProvince());
        addressTxf.setText(supplier.getAddress());
        coOrdinatesTxf.setText(supplier.getCoOrdinates());
        populateContacts();
    }

    private void populateContacts(){
        ObservableList<HBox> contactCards = FXCollections.observableArrayList();
        for (ContactDetails c: supplier.getContactDetails()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ViewSuppliersContactCardPane.fxml"));
            HBox root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ViewSuppliersContactCardPaneController csccpc = loader.getController();
            csccpc.initData(c, supplier);

            contactCards.add(root);
        }
        Platform.runLater(() -> {
            contactsList.getChildren().clear();
            contactsList.getChildren().addAll(contactCards);
        });
    }

    public void addButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewSupplierContactPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewSupplierContactPaneController nscpc = loader.getController();
        nscpc.initData(supplier);
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SuppliersPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
