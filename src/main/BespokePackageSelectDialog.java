package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import models.*;

import java.util.ArrayList;
import java.util.List;

public class BespokePackageSelectDialog extends CustomDialogSkin {

    ComboBox provinceCmb;
    ComboBox supplierCmb;
    ListView productListView;

    public Product BespokePackageSelectDialog(Window parent, String category, Product product) {
        final Product[] selectedProduct = new Product[1];
        initOwner(parent);
        Text headingText = new Text("Select " + category + " for the package:");
        headingText.getStyleClass().add("secondaryHeadingText");
        Text provinceLbl = new Text("Province:");
        provinceLbl.getStyleClass().add("tertiaryHeadingText");
        provinceCmb = new ComboBox();
        provinceCmb.getItems().clear();
        provinceCmb.getItems().addAll("Select Province", "All", "Western Cape", "Eastern Cape", "Northern Cape", "Gauteng", "Kwa-zulu Natal", "North West", "Mpumulanga", "Limpopo", "Free-State", "South Africa", "World");
        provinceCmb.getSelectionModel().select("Select Province");
        Text supplierLbl = new Text("Supplier:");
        supplierLbl.getStyleClass().add("tertiaryHeadingText");
        supplierCmb = new ComboBox();
        supplierCmb.getItems().add("Select Province First");
        supplierCmb.getSelectionModel().select("Select Province First");
        provinceCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            supplierCmb.getItems().clear();
            supplierCmb.getItems().add("Select Supplier");
            supplierCmb.getSelectionModel().select("Select Supplier");
            supplierCmb.getItems().addAll(getSuppliers(category, newItem.toString()));
        });
        Text productLbl = new Text("Products:");
        productLbl.getStyleClass().add("tertiaryHeadingText");
        productListView = new ListView();
        productListView.getStyleClass().add("dialog-products-ListView");
        supplierCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            populateList(category);
        });
        Button selectBtn = new Button("Select");
        selectBtn.getStyleClass().add("dialog-button");
        selectBtn.setOnAction(event -> {
            if(productListView.getSelectionModel().getSelectedItem() != null){
                selectedProduct[0] = (Product) productListView.getSelectionModel().getSelectedItem();
                this.closeAnimation();
            } else {
                UserNotification.showMessage(Main.stage, "Select Product", "Select Product from list before adding.");
            }
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("dialog-product-button");
        cancelBtn.setOnAction(event -> {
            this.closeAnimation();
        });

        HBox province = new HBox(provinceLbl, provinceCmb);
        province.setAlignment(Pos.CENTER);
        province.setSpacing(10);
        HBox supplier = new HBox(supplierLbl, supplierCmb);
        supplier.setAlignment(Pos.CENTER);
        supplier.setSpacing(10);
        VBox products = new VBox(productLbl, productListView);
        products.setAlignment(Pos.CENTER);
        HBox buttons = new HBox(selectBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        if(product != null){
            if(category.matches("Accommodation")) {
                provinceCmb.getSelectionModel().select(((ProductAccomodation) product).getProvince());
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add(getSuppliers(category, provinceCmb.getSelectionModel().getSelectedItem().toString()));
                supplierCmb.getSelectionModel().select(((ProductAccomodation) product).getSupplierName());
                populateList(category);
            } else if(category.matches("Golf")) {
                provinceCmb.getSelectionModel().select(((ProductGolf) product).getProvince());
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add(getSuppliers(category, provinceCmb.getSelectionModel().getSelectedItem().toString()));
                supplierCmb.getSelectionModel().select(((ProductGolf) product).getSupplierName());
                populateList(category);
            } else if(category.matches("Transport")) {
                provinceCmb.getSelectionModel().select(((ProductTransport) product).getProvince());
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add(getSuppliers(category, provinceCmb.getSelectionModel().getSelectedItem().toString()));
                supplierCmb.getSelectionModel().select(((ProductTransport) product).getSupplierName());
                populateList(category);
            } else if(category.matches("Activities")) {
                provinceCmb.getSelectionModel().select(((ProductActivity) product).getProvince());
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add(getSuppliers(category, provinceCmb.getSelectionModel().getSelectedItem().toString()));
                supplierCmb.getSelectionModel().select(((ProductActivity) product).getSupplierName());
                populateList(category);
            }
        }
        VBox settingsInnerPane = new VBox(headingText, province, supplier, products, buttons);
        settingsInnerPane.getChildren().addAll();
        settingsInnerPane.setSpacing(15);
        settingsInnerPane.setPadding(new Insets(20));
        settingsInnerPane.setAlignment(Pos.TOP_CENTER);
        settingsInnerPane.setStyle("-fx-background-color: #007FA3;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;");
        settingsInnerPane.setMaxSize(650, 500);
        settingsInnerPane.setMinSize(650, 500);
        VBox settingsPane = new VBox(settingsInnerPane);
        setWidth(650);
        settingsPane.setAlignment(Pos.CENTER);
        getDialogPane().setContent(settingsPane);
        showDialog();
        return selectedProduct[0];
    }

    private List<String>getSuppliers(String category, String province){
        List<String>suppliers = new ArrayList<>();
        if(category.matches("Accommodation")) {
            for (Supplier s : Main.connectionHandler.suppliers) {
                if(s.getProvince().matches(province)) {
                    suppliers.add(s.getSupplierName());
                }
            }
        } else if(category.matches("Golf")) {
            for (ProductGolf p : Main.connectionHandler.golf) {
                for (Supplier s : Main.connectionHandler.suppliers) {
                    if(s.getProvince().matches(province)) {
                        suppliers.add(s.getSupplierName());
                    }
                }
            }
        } else if(category.matches("Transport")) {
            for (ProductTransport p : Main.connectionHandler.transport) {
                for (Supplier s : Main.connectionHandler.suppliers) {
                    if(s.getProvince().matches(province)) {
                        suppliers.add(s.getSupplierName());
                    }
                }
            }
        } else if(category.matches("Activity")) {
            for (ProductActivity p : Main.connectionHandler.activities) {
                for (Supplier s : Main.connectionHandler.suppliers) {
                    if(s.getProvince().matches(province)) {
                        suppliers.add(s.getSupplierName());
                    }
                }
            }
        }
        return suppliers;
    }

    private void populateList(String category) {
        List<Product> products = new ArrayList<>();
        if(category.matches("Accommodation")){
            for (Product p: Main.connectionHandler.accomodation){
                if(((ProductAccomodation)p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductAccomodation)p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())){
                    products.add(p);
                }
            }
        } else if(category.matches("Golf")){
            for (Product p: Main.connectionHandler.golf){
                if(((ProductGolf)p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductGolf)p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())){
                    products.add(p);
                }
            }
        } else if(category.matches("Transport")){
            for (Product p: Main.connectionHandler.transport){
                if(((ProductTransport)p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductTransport)p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())){
                    products.add(p);
                }
            }
        } else if(category.matches("Activities")){
            for (Product p: Main.connectionHandler.activities){
                if(((ProductActivity)p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductActivity)p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())){
                    products.add(p);
                }
            }
        }
        productListView.getItems().clear();
        productListView.getItems().addAll(products);
    }
}
