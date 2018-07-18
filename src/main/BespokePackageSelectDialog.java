package main;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
        provinceCmb.getItems().addAll("Select Province", "All", "Western Cape", "Eastern Cape", "Northern Cape", "Gauteng", "Kwa-zulu Natal", "North West", "Mpumalanga", "Limpopo", "Free-State", "South Africa", "World");
        provinceCmb.getSelectionModel().select("Select Province");
        provinceCmb.setPrefWidth(200);
        provinceCmb.setPrefHeight(25);
        provinceCmb.setStyle("-fx-font-size: 15");
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
        supplierCmb.setPrefWidth(200);
        supplierCmb.setPrefHeight(25);
        supplierCmb.setStyle("-fx-font-size: 15");
        Text productLbl = new Text("Products:");
        productLbl.getStyleClass().add("tertiaryHeadingText");
        productListView = new ListView();
        productListView.getStyleClass().add("dialog-products-ListView");
        supplierCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            populateList(category);
        });
        Text rackLbl = new Text("Rack: R");
        rackLbl.getStyleClass().add("tertiaryHeadingText");
        TextField rackTxf = new TextField();
        rackTxf.setPrefWidth(120);
        rackTxf.setPrefHeight(30);
        rackTxf.setStyle("-fx-font-size: 15");
        HBox rackHbox = new HBox(rackLbl, rackTxf);
        rackHbox.setAlignment(Pos.CENTER);
        productListView.setOnMouseClicked(e -> {
            if(productListView.getSelectionModel().getSelectedItem() != null){
                if(category.matches("Accommodation")){
                    Double x = (Double.parseDouble(((ProductAccommodation)productListView.getSelectionModel().getSelectedItem()).getPrice()) / (100 - Double.parseDouble(((ProductAccommodation)productListView.getSelectionModel().getSelectedItem()).getCommission())) * 100);
                    rackTxf.setText((Math.round(x/10f)*10) + 10 + "");
                } else if(category.matches("Golf")){
                    Double x = (Double.parseDouble(((ProductGolf)productListView.getSelectionModel().getSelectedItem()).getPrice()) / (100 - Double.parseDouble(((ProductGolf)productListView.getSelectionModel().getSelectedItem()).getCommission())) * 100);
                    rackTxf.setText((Math.round(x/10f)*10) + 10 + "");
                } else if(category.matches("Transport")){
                    Double x = (Double.parseDouble(((ProductTransport)productListView.getSelectionModel().getSelectedItem()).getPrice()) / (100 - Double.parseDouble(((ProductTransport)productListView.getSelectionModel().getSelectedItem()).getCommission())) * 100);
                    rackTxf.setText((Math.round(x/10f)*10) + 10 + "");
                } else if(category.matches("Activity")){
                    Double x = (Double.parseDouble(((ProductActivity)productListView.getSelectionModel().getSelectedItem()).getPrice()) / (100 - Double.parseDouble(((ProductActivity)productListView.getSelectionModel().getSelectedItem()).getCommission())) * 100);
                    rackTxf.setText((Math.round(x/10f)*10) + 10 + "");
                }
            }
        });
        Text quantityLbl = new Text("Quantity:");
        quantityLbl.getStyleClass().add("tertiaryHeadingText");
        ComboBox quantityCmb = new ComboBox();
        for (int i = 0; i < 501; i++){
            quantityCmb.getItems().add(i);
        }
        quantityCmb.getSelectionModel().select(0);
        quantityCmb.setPrefWidth(90);
        quantityCmb.setPrefHeight(30);
        quantityCmb.setStyle("-fx-font-size: 15");
        Text nightsRoundsLbl = new Text("Nights-Rounds");
        nightsRoundsLbl.getStyleClass().add("tertiaryHeadingText");
        ComboBox nightsRoundsCmb = new ComboBox();
        for (int i = 0; i < 501; i++){
            nightsRoundsCmb.getItems().add(i);
        }
        nightsRoundsCmb.getSelectionModel().select(0);
        nightsRoundsCmb.setPrefWidth(90);
        nightsRoundsCmb.setPrefHeight(30);
        nightsRoundsCmb.setStyle("-fx-font-size: 15");
        if(category.matches("Accommodation")){
            nightsRoundsLbl.setText("Nights:");
        } else if (category.matches("Golf")) {
            nightsRoundsLbl.setText("Rounds:");
        } else {
            nightsRoundsLbl.setVisible(false);
            nightsRoundsCmb.setVisible(false);
        }
        HBox cmbSelection = new HBox(quantityLbl, quantityCmb, nightsRoundsLbl, nightsRoundsCmb);
        cmbSelection.setAlignment(Pos.CENTER);
        cmbSelection.setSpacing(10);
        Text addtoLbl = new Text("Add to:");
        addtoLbl.getStyleClass().add("tertiaryHeadingText");
        ComboBox addToCmb = new ComboBox();
        addToCmb.getItems().addAll("Please Select", "Golfer Sharing", "Non-Golfer Sharing", "Golfer and Non-Golfer Sharing", "Golfer Single", "Non-Golfer Single");
        addToCmb.getSelectionModel().select(0);
        addToCmb.setPrefWidth(280);
        addToCmb.setPrefHeight(30);
        addToCmb.setStyle("-fx-font-size: 15");
        HBox addToSelection = new HBox(addtoLbl, addToCmb);
        addToSelection.setAlignment(Pos.CENTER);
        addToSelection.setSpacing(10);
        Button selectBtn = new Button("Select");
        selectBtn.getStyleClass().add("dialog-button");
        selectBtn.setOnAction(event -> {
            if (productListView.getSelectionModel().getSelectedItem() != null) {
                if(!nightsRoundsCmb.getSelectionModel().getSelectedItem().toString().matches("0")||category.matches("Transport")||category.matches("Activity")) {
                    if(!quantityCmb.getSelectionModel().getSelectedItem().toString().matches("0")) {
                        if(!addToCmb.getSelectionModel().getSelectedItem().toString().matches("Please Select")) {
                            if (category.matches("Accommodation")) {
                                if (Integer.parseInt(rackTxf.getText()) > Integer.parseInt(((ProductAccommodation) productListView.getSelectionModel().getSelectedItem()).getPrice())) {
                                    ProductAccommodation x = (ProductAccommodation) productListView.getSelectionModel().getSelectedItem();
                                    selectedProduct[0] = new BookingAccommodation(x.getSupplierName(), x.getProvince(), x.getProductName(), Integer.toString(x.getSleeps()), "", (int) nightsRoundsCmb.getSelectionModel().getSelectedItem(), (int) quantityCmb.getSelectionModel().getSelectedItem(),  Integer.parseInt(x.getPrice()) + 0.00, Integer.parseInt(rackTxf.getText()) + 0.00, addToCmb.getSelectionModel().getSelectedItem().toString(), 0, 0.0);
                                    //TODO Arrival Date in popup
                                    this.closeAnimation();
                                } else {
                                    new CustomDialog(Main.stage, "Rack Rate is Less than STO", "Increase Rack rate to more than STO.", new JFXButton("Ok")).showDialog();
                                }
                            } else if (category.matches("Golf")) {
                                if (Integer.parseInt(rackTxf.getText()) > Integer.parseInt(((ProductGolf) selectedProduct[0]).getPrice())) {
                                    ProductGolf x = (ProductGolf) productListView.getSelectionModel().getSelectedItem();
                                    selectedProduct[0] = new BookingGolf(x.getSupplierName(), x.getProvince(), x.getProductName(), null,(int) quantityCmb.getSelectionModel().getSelectedItem(), (int) nightsRoundsCmb.getSelectionModel().getSelectedItem(), x.getCarts(), Integer.parseInt(x.getPrice()) + 0.00, Integer.parseInt(rackTxf.getText()) + 0.00, addToCmb.getSelectionModel().getSelectedItem().toString(),0, 0.0);
                                    //TODO
                                    this.closeAnimation();
                                } else {
                                    new CustomDialog(Main.stage, "Rack Rate is Less than STO", "Increase Rack rate to more than STO.", new JFXButton("Ok")).showDialog();
                                }
                            } else if (category.matches("Transport")) {
                                if (Integer.parseInt(rackTxf.getText()) > Integer.parseInt(((ProductTransport) selectedProduct[0]).getPrice())) {
                                    ProductTransport x = (ProductTransport) productListView.getSelectionModel().getSelectedItem();
                                    selectedProduct[0] = new BookingTransport(x.getSupplierName(), x.getProvince(), x.getProductName(), "", (int) quantityCmb.getSelectionModel().getSelectedItem(), "", "",Integer.parseInt(x.getPrice()) + 0.00, Integer.parseInt(rackTxf.getText()) + 0.00, addToCmb.getSelectionModel().getSelectedItem().toString(),  0, 0.0);
                                    //TODO
                                    this.closeAnimation();
                                } else {
                                    new CustomDialog(Main.stage, "Rack Rate is Less than STO", "Increase Rack rate to more than STO.", new JFXButton("Ok")).showDialog();
                                }
                            } else if (category.matches("Activity")) {
                                if (Integer.parseInt(rackTxf.getText()) > Integer.parseInt(((ProductActivity) selectedProduct[0]).getPrice())) {
                                    ProductActivity x = (ProductActivity) productListView.getSelectionModel().getSelectedItem();
                                    selectedProduct[0] = new BookingActivity(x.getSupplierName(), x.getProvince(), x.getProductName(), "", (int) quantityCmb.getSelectionModel().getSelectedItem(), Integer.parseInt(x.getPrice()) + 0.00, Integer.parseInt(rackTxf.getText()) + 0.00, addToCmb.getSelectionModel().getSelectedItem().toString(), 0, 0.0);
                                    //TODO
                                    this.closeAnimation();
                                } else {
                                    new CustomDialog(Main.stage, "Rack Rate is Less than STO", "Increase Rack rate to more than STO.", new JFXButton("Ok")).showDialog();
                                }
                            }
                        } else {
                            new CustomDialog(Main.stage, "Add to not selected", "Select to which group the product must be added to.", new JFXButton("Ok")).showDialog();
                        }
                    } else {
                        new CustomDialog(Main.stage, "Quantity not selected", "Select the quantity of the product needed for the tour.", new JFXButton("Ok")).showDialog();
                    }
                } else {
                    if (category.matches("Accommodation")) {
                        new CustomDialog(Main.stage, "Nights not selected", "Select the amount of nights the tour will be staying here.", new JFXButton("Ok")).showDialog();
                    } else if (category.matches("Golf")){
                        new CustomDialog(Main.stage, "Rounds not selected", "Select the amount of rounds the golfers will be play here.", new JFXButton("Ok")).showDialog();
                    }
                }
            } else {
                new CustomDialog(Main.stage, "Select Product", "Select Product from list before adding.", new JFXButton("Ok")).showDialog();;
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
                provinceCmb.getSelectionModel().select(((BookingAccommodation) product).getProvince());
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add(getSuppliers(category, provinceCmb.getSelectionModel().getSelectedItem().toString()));
                supplierCmb.getSelectionModel().select(((BookingAccommodation) product).getSupplierName());
                populateList(category);
                for (int i = 0; i < productListView.getItems().size(); i++) {
                    if(((ProductAccommodation)productListView.getItems().get(i)).getSupplierName().matches(((BookingAccommodation)product).getSupplierName())){
                        productListView.getSelectionModel().select(i);
                    }
                }
            } else if(category.matches("Golf")) {
                provinceCmb.getSelectionModel().select(((BookingGolf) product).getProvince());
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add(getSuppliers(category, provinceCmb.getSelectionModel().getSelectedItem().toString()));
                supplierCmb.getSelectionModel().select(((BookingGolf) product).getSupplierName());
                populateList(category);
                for (int i = 0; i < productListView.getItems().size(); i++) {
                    if(((ProductGolf)productListView.getItems().get(i)).getSupplierName().matches(((BookingGolf)product).getSupplierName())){
                        productListView.getSelectionModel().select(i);
                    }
                }
            } else if(category.matches("Transport")) {
                provinceCmb.getSelectionModel().select(((BookingTransport) product).getProvince());
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add(getSuppliers(category, provinceCmb.getSelectionModel().getSelectedItem().toString()));
                supplierCmb.getSelectionModel().select(((BookingTransport) product).getSupplierName());
                populateList(category);
                for (int i = 0; i < productListView.getItems().size(); i++) {
                    if(((ProductTransport)productListView.getItems().get(i)).getSupplierName().matches(((BookingTransport)product).getSupplierName())){
                        productListView.getSelectionModel().select(i);
                    }
                }
            } else if(category.matches("Activity")) {
                provinceCmb.getSelectionModel().select(((BookingActivity) product).getProvince());
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add(getSuppliers(category, provinceCmb.getSelectionModel().getSelectedItem().toString()));
                supplierCmb.getSelectionModel().select(((BookingActivity) product).getSupplierName());
                populateList(category);
                for (int i = 0; i < productListView.getItems().size(); i++) {
                    if(((ProductActivity)productListView.getItems().get(i)).getSupplierName().matches(((BookingActivity)product).getSupplierName())){
                        productListView.getSelectionModel().select(i);
                    }
                }
            }
        }
        VBox settingsInnerPane = new VBox(headingText, province, supplier, products, rackHbox, cmbSelection, addToSelection, buttons);
        settingsInnerPane.getChildren().addAll();
        settingsInnerPane.setSpacing(15);
        settingsInnerPane.setPadding(new Insets(20));
        settingsInnerPane.setAlignment(Pos.TOP_CENTER);
        settingsInnerPane.setStyle("-fx-background-color: #595959;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;");
        settingsInnerPane.setMaxSize(750, 650);
        settingsInnerPane.setMinSize(750, 650);
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
        try {
            if (category.matches("Accommodation")) {
                for (Product p : Main.connectionHandler.accomodation) {
                    if (((ProductAccommodation) p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductAccommodation) p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())) {
                        products.add(p);
                    }
                }
            } else if (category.matches("Golf")) {
                for (Product p : Main.connectionHandler.golf) {
                    if (((ProductGolf) p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductGolf) p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())) {
                        products.add(p);
                    }
                }
            } else if (category.matches("Transport")) {
                for (Product p : Main.connectionHandler.transport) {
                    if (((ProductTransport) p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductTransport) p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())) {
                        products.add(p);
                    }
                }
            } else if (category.matches("Activitiy")) {
                for (Product p : Main.connectionHandler.activities) {
                    if (((ProductActivity) p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductActivity) p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())) {
                        products.add(p);
                    }
                }
            }
        } catch(NullPointerException ex) {

        }
        Platform.runLater(() -> {
            productListView.getItems().clear();
            productListView.getItems().addAll(products);
        });
    }
}
