package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewSuppliersPaneController implements Initializable {

    @FXML Label supplierNameTxf;
    @FXML Label provinceTxf;
    @FXML Label categoryTxf;
    @FXML Label addressTxf;
    @FXML ScrollPane contactsScrollPane;
    @FXML VBox contactsList;
    @FXML ListView productsListView;
    @FXML ListView pricesListView;
    private Supplier supplier;
    private String category;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Supplier supplier, String category){
        this.supplier = supplier;
        this.category = category;
        supplierNameTxf.setText(supplier.getSupplierName());
        categoryTxf.setText(supplier.getCategory());
        provinceTxf.setText(supplier.getProvince());
        addressTxf.setText(supplier.getAddress());
        if(!supplier.getContactDetails().isEmpty()){
            populateContacts();
        } else {
            Platform.runLater(() -> contactsList.getChildren().clear());
        }
        populateProducts();
        productsListView.setOnMouseClicked(e -> {
            if(productsListView.getSelectionModel().getSelectedItem() != null){
                if (category.matches("Accommodation")) {
                    ProductAccommodation p = (ProductAccommodation) productsListView.getSelectionModel().getSelectedItem();
                    pricesListView.getItems().clear();
                    for (String[] s : p.getPrices()) {
                        pricesListView.getItems().add("Valid: " + s[0] + " - " + s[1] + " Price: R " + s[2]);
                    }
                } else if (category.matches("Golf")) {
                    ProductGolf p = (ProductGolf) productsListView.getSelectionModel().getSelectedItem();
                    pricesListView.getItems().clear();
                    for (String[] s : p.getPrices()) {
                        pricesListView.getItems().add("Valid: " + s[0] + " - " + s[1] + " Price: R " + s[2]);
                    }
                } else if (category.matches("Transport")) {
                    ProductTransport p = (ProductTransport) productsListView.getSelectionModel().getSelectedItem();
                    pricesListView.getItems().clear();
                    for (String[] s : p.getPrices()) {
                        pricesListView.getItems().add("Valid: " + s[0] + " - " + s[1] + " Price: R " + s[2]);
                    }

                } else if (category.matches("Activities")) {
                    ProductActivity p = (ProductActivity) productsListView.getSelectionModel().getSelectedItem();
                    pricesListView.getItems().clear();
                    for (String[] s : p.getPrices()) {
                        pricesListView.getItems().add("Valid: " + s[0] + " - " + s[1] + " Price: R " + s[2]);
                    }
                }
            }
        });
    }

    private void populateContacts(){
        Platform.runLater(() -> contactsList.getChildren().clear());
        ObservableList<HBox> contactCards = FXCollections.observableArrayList();
        if (!supplier.getContactDetails().isEmpty()) {
            for (ContactDetails cd : supplier.getContactDetails()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("ViewSuppliersContactCardPane.fxml"));
                HBox root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ViewSuppliersContactCardPaneController vsccpc = loader.getController();
                vsccpc.initData(cd, supplier, category);
                contactCards.add(root);
            }
            Platform.runLater(() -> contactsList.getChildren().addAll(contactCards));
        }
    }

    private void populateProducts(){
        List<Product> products = new ArrayList<>();
        if(category.matches("Accommodation")){
            for (ProductAccommodation pa:Main.connectionHandler.accomodation){
                if(pa.getSupplierName().matches(supplier.getSupplierName())) {
                    pa.setDateSelected("All");
                    products.add(pa);
                }
            }
        }
        if(category.matches("Golf")) {
            for (ProductGolf pg:Main.connectionHandler.golf){
                if(pg.getSupplierName().matches(supplier.getSupplierName())) {
                    pg.setDateSelected("All");
                    products.add(pg);
                }
            }
        }
        if(category.matches("Transport")) {
            for (ProductTransport pt:Main.connectionHandler.transport){
                if(pt.getSupplierName().matches(supplier.getSupplierName())) {
                    pt.setDateSelected("All");
                    products.add(pt);
                }
            }
        }
        if(category.matches("Activities")) {
            for (ProductActivity pa:Main.connectionHandler.activities){
                if(pa.getSupplierName().matches(supplier.getSupplierName())) {
                    pa.setDateSelected("All");
                    products.add(pa);
                }
            }
        }
        Platform.runLater(() -> {
            productsListView.getItems().clear();
            productsListView.getItems().addAll(products);
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
        nscpc.initData(supplier, category);
    }

    public void editButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewSupplierPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewSupplierPaneController nspc = loader.getController();
        nspc.initData(supplier, "ViewSuppliersPane", category);
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SuppliersPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SuppliersPaneController spc = loader.getController();
        spc.initData(category);
    }

}
