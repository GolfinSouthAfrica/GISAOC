package main;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;
import javafx.util.StringConverter;
import models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        provinceLbl.setWrappingWidth(60);
        provinceLbl.setStrokeWidth(60);
        provinceLbl.setTextAlignment(TextAlignment.RIGHT);
        provinceCmb = new ComboBox();
        provinceCmb.getItems().clear();
        provinceCmb.getItems().addAll("Select Province", "All", "Western Cape", "Eastern Cape", "Northern Cape", "Gauteng", "Kwa-Zulu Natal", "North West", "Mpumalanga", "Limpopo", "Free-State", "South Africa", "World");
        provinceCmb.getSelectionModel().select("Select Province");
        provinceCmb.setPrefWidth(200);
        provinceCmb.setPrefHeight(25);
        provinceCmb.setStyle("-fx-font-size: 15");
        DatePicker dateDP = new DatePicker();
        dateDP.setConverter(new StringConverter<LocalDate>(){
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");
            @Override
            public String toString(LocalDate localDate){
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }
            @Override
            public LocalDate fromString(String dateString){
                if(dateString==null || dateString.trim().isEmpty()){
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
        dateDP.setOnAction(event -> {
            if(dateDP.getValue()!=null) {
                populateList(category, dateDP.getValue().toString());
            } else {
                productListView.getItems().clear();
            }
        });
        dateDP.setPrefWidth(200);
        dateDP.setPrefHeight(25);
        Text supplierLbl = new Text("Supplier:");
        supplierLbl.getStyleClass().add("tertiaryHeadingText");
        supplierLbl.setWrappingWidth(60);
        supplierLbl.setStrokeWidth(60);
        supplierLbl.setTextAlignment(TextAlignment.RIGHT);
        supplierCmb = new ComboBox();
        supplierCmb.getItems().add("Select Province First");
        supplierCmb.getSelectionModel().select("Select Province First");
        provinceCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            dateDP.setValue(null);
            supplierCmb.getItems().clear();
            supplierCmb.getItems().add("Select Supplier");
            supplierCmb.getSelectionModel().select("Select Supplier");
            supplierCmb.getItems().addAll(getSuppliers(category, newItem.toString()));
        });
        supplierCmb.setPrefWidth(200);
        supplierCmb.setPrefHeight(25);
        supplierCmb.setStyle("-fx-font-size: 15");
        supplierCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            dateDP.setValue(null);
        });
        Text dateLbl = new Text("Date:");
        dateLbl.getStyleClass().add("tertiaryHeadingText");
        dateLbl.setWrappingWidth(60);
        dateLbl.setStrokeWidth(60);
        dateLbl.setTextAlignment(TextAlignment.RIGHT);
        Text productLbl = new Text("Products:");
        productLbl.getStyleClass().add("tertiaryHeadingText");
        productListView = new ListView();
        productListView.getStyleClass().add("dialog-products-ListView");
        Text stoLbl = new Text("STO Rate: R");
        stoLbl.getStyleClass().add("tertiaryHeadingText");
        stoLbl.setWrappingWidth(80);
        stoLbl.setStrokeWidth(80);
        stoLbl.setTextAlignment(TextAlignment.RIGHT);
        TextField stoTxf = new TextField();
        stoTxf.setPrefWidth(120);
        stoTxf.setPrefHeight(30);
        stoTxf.setStyle("-fx-font-size: 15");
        HBox stoHbox = new HBox(stoLbl, stoTxf);
        stoHbox.setAlignment(Pos.CENTER);
        Text rackLbl = new Text("Asking Client: R");
        rackLbl.getStyleClass().add("tertiaryHeadingText");
        rackLbl.setWrappingWidth(80);
        rackLbl.setStrokeWidth(80);
        rackLbl.setTextAlignment(TextAlignment.RIGHT);
        TextField rackTxf = new TextField();
        rackTxf.setPrefWidth(120);
        rackTxf.setPrefHeight(30);
        rackTxf.setStyle("-fx-font-size: 15");
        HBox rackHbox = new HBox(rackLbl, rackTxf);
        rackHbox.setAlignment(Pos.CENTER);
        productListView.setOnMouseClicked(e -> {
            if(productListView.getSelectionModel().getSelectedItem() != null){
                try {
                    if (category.matches("Accommodation")) {
                        Double x = (Double.parseDouble(((ProductAccommodation) productListView.getSelectionModel().getSelectedItem()).getPrice()) / (100 - Double.parseDouble(((ProductAccommodation) productListView.getSelectionModel().getSelectedItem()).getCommission())) * 100);
                        int y = Integer.parseInt(((ProductAccommodation) productListView.getSelectionModel().getSelectedItem()).getPrice());
                        rackTxf.setText((Math.round(x / 10f) * 10) + 10 + "");
                        stoTxf.setText(y + "");
                    } else if (category.matches("Golf")) {
                        Double x = (Double.parseDouble(((ProductGolf) productListView.getSelectionModel().getSelectedItem()).getPrice()) / (100 - Double.parseDouble(((ProductGolf) productListView.getSelectionModel().getSelectedItem()).getCommission())) * 100);
                        int y = Integer.parseInt(((ProductGolf) productListView.getSelectionModel().getSelectedItem()).getPrice());
                        rackTxf.setText((Math.round(x / 10f) * 10) + 10 + "");
                        stoTxf.setText(y + "");
                    } else if (category.matches("Transport")) {
                        Double x = (Double.parseDouble(((ProductTransport) productListView.getSelectionModel().getSelectedItem()).getPrice()) / (100 - Double.parseDouble(((ProductTransport) productListView.getSelectionModel().getSelectedItem()).getCommission())) * 100);
                        int y = Integer.parseInt(((ProductTransport) productListView.getSelectionModel().getSelectedItem()).getPrice());
                        rackTxf.setText((Math.round(x / 10f) * 10) + 10 + "");
                        stoTxf.setText(y + "");
                    } else if (category.matches("Activity")) {
                        Double x = (Double.parseDouble(((ProductActivity) productListView.getSelectionModel().getSelectedItem()).getPrice()) / (100 - Double.parseDouble(((ProductActivity) productListView.getSelectionModel().getSelectedItem()).getCommission())) * 100);
                        int y = Integer.parseInt(((ProductActivity) productListView.getSelectionModel().getSelectedItem()).getPrice());
                        rackTxf.setText((Math.round(x / 10f) * 10) + 10 + "");
                        stoTxf.setText(y + "");
                    }
                } catch (NumberFormatException ex) {

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
                                    selectedProduct[0] = new BookingAccommodation(-1, x.getSupplierName(), x.getProvince(), x.getProductName(), Integer.toString(x.getSleeps()), dateDP.getValue().toString(), (int) nightsRoundsCmb.getSelectionModel().getSelectedItem(), (int) quantityCmb.getSelectionModel().getSelectedItem(),  Integer.parseInt(stoTxf.getText()) + 0.00, Integer.parseInt(rackTxf.getText()) + 0.00, addToCmb.getSelectionModel().getSelectedItem().toString(), 0, 0.0);
                                    //TODO Arrival Date in popup
                                    this.closeAnimation();
                                } else {
                                    new CustomDialog().CustomDialog(Main.stage, "Rack Rate is Less than STO", "Increase Rack rate to more than STO.", new JFXButton("Ok"));
                                }
                            } else if (category.matches("Golf")) {
                                if (Integer.parseInt(rackTxf.getText()) > Integer.parseInt(((ProductGolf) selectedProduct[0]).getPrice())) {
                                    ProductGolf x = (ProductGolf) productListView.getSelectionModel().getSelectedItem();
                                    selectedProduct[0] = new BookingGolf(-1, x.getSupplierName(), x.getProvince(), x.getProductName(), null,(int) quantityCmb.getSelectionModel().getSelectedItem(), (int) nightsRoundsCmb.getSelectionModel().getSelectedItem(), Integer.parseInt(stoTxf.getText()) + 0.00, Integer.parseInt(rackTxf.getText()) + 0.00, addToCmb.getSelectionModel().getSelectedItem().toString(),0, 0.0);
                                    //TODO
                                    this.closeAnimation();
                                } else {
                                    new CustomDialog().CustomDialog(Main.stage, "Rack Rate is Less than STO", "Increase Rack rate to more than STO.", new JFXButton("Ok"));
                                }
                            } else if (category.matches("Transport")) {
                                if (Integer.parseInt(rackTxf.getText()) > Integer.parseInt(((ProductTransport) selectedProduct[0]).getPrice())) {
                                    ProductTransport x = (ProductTransport) productListView.getSelectionModel().getSelectedItem();
                                    selectedProduct[0] = new BookingTransport(-1, x.getSupplierName(), x.getProvince(), x.getProductName(), dateDP.getValue().toString(), (int) quantityCmb.getSelectionModel().getSelectedItem(), "", "",Integer.parseInt(stoTxf.getText()) + 0.00, Integer.parseInt(rackTxf.getText()) + 0.00, addToCmb.getSelectionModel().getSelectedItem().toString(),  0, 0.0);
                                    //TODO
                                    this.closeAnimation();
                                } else {
                                    new CustomDialog().CustomDialog(Main.stage, "Rack Rate is Less than STO", "Increase Rack rate to more than STO.", new JFXButton("Ok"));
                                }
                            } else if (category.matches("Activity")) {
                                if (Integer.parseInt(rackTxf.getText()) > Integer.parseInt(((ProductActivity) selectedProduct[0]).getPrice())) {
                                    ProductActivity x = (ProductActivity) productListView.getSelectionModel().getSelectedItem();
                                    selectedProduct[0] = new BookingActivity(-1, x.getSupplierName(), x.getProvince(), x.getProductName(), dateDP.getValue().toString(), (int) quantityCmb.getSelectionModel().getSelectedItem(), Integer.parseInt(stoTxf.getText()) + 0.00, Integer.parseInt(rackTxf.getText()) + 0.00, addToCmb.getSelectionModel().getSelectedItem().toString(), 0, 0.0);
                                    //TODO
                                    this.closeAnimation();
                                } else {
                                    new CustomDialog().CustomDialog(Main.stage, "Rack Rate is Less than STO", "Increase Rack rate to more than STO.", new JFXButton("Ok"));
                                }
                            }
                        } else {
                            new CustomDialog().CustomDialog(Main.stage, "Add to not selected", "Select to which group the product must be added to.", new JFXButton("Ok"));
                        }
                    } else {
                        new CustomDialog().CustomDialog(Main.stage, "Quantity not selected", "Select the quantity of the product needed for the tour.", new JFXButton("Ok"));
                    }
                } else {
                    if (category.matches("Accommodation")) {
                        new CustomDialog().CustomDialog(Main.stage, "Nights not selected", "Select the amount of nights the tour will be staying here.", new JFXButton("Ok"));
                    } else if (category.matches("Golf")){
                        new CustomDialog().CustomDialog(Main.stage, "Rounds not selected", "Select the amount of rounds the golfers will be play here.", new JFXButton("Ok"));
                    }
                }
            } else {
                new CustomDialog().CustomDialog(Main.stage, "Select Product", "Select Product from list before adding.", new JFXButton("Ok"));;
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
        HBox dateBox = new HBox(dateLbl, dateDP);
        dateBox.setAlignment(Pos.CENTER);
        dateBox.setSpacing(10);
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
                //populateList(category, ((ProductAccommodation)product).getS);
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
                //populateList(category);
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
                //populateList(category);
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
                //populateList(category);
                for (int i = 0; i < productListView.getItems().size(); i++) {
                    if(((ProductActivity)productListView.getItems().get(i)).getSupplierName().matches(((BookingActivity)product).getSupplierName())){
                        productListView.getSelectionModel().select(i);
                    }
                }
            }
        }
        VBox settingsInnerPane = new VBox(headingText, province, supplier, dateBox, products, stoHbox, rackHbox, cmbSelection, addToSelection, buttons);
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
                if(s.getProvince().matches(province) || province.matches("All")) {
                    if(s.getCategory().matches(category)) {
                        if(!suppliers.contains(s.getSupplierName())) {
                            suppliers.add(s.getSupplierName());
                        }
                    }
                }
            }
        } else if(category.matches("Golf")) {
            for (Supplier s : Main.connectionHandler.suppliers) {
                if(s.getProvince().matches(province) || province.matches("All")) {
                    if(s.getCategory().matches(category)) {
                        if(!suppliers.contains(s.getSupplierName())) {
                            suppliers.add(s.getSupplierName());
                        }
                    }
                }
            }
        } else if(category.matches("Transport")) {
            for (Supplier s : Main.connectionHandler.suppliers) {
                if(s.getProvince().matches(province) || province.matches("All")) {
                    if(s.getCategory().matches(category)) {
                        if(!suppliers.contains(s.getSupplierName())) {
                            suppliers.add(s.getSupplierName());
                        }
                    }
                }
            }
        } else if(category.matches("Activity")) {
            for (Supplier s : Main.connectionHandler.suppliers) {
                if(s.getProvince().matches(province) || province.matches("All")) {
                    if(s.getCategory().matches(category)) {
                        if(!suppliers.contains(s.getSupplierName())) {
                            suppliers.add(s.getSupplierName());
                        }
                    }
                }
            }
        }
        return suppliers;
    }

    private void populateList(String category, String date) {
        List<Product> products = new ArrayList<>();
        try {
            if (category.matches("Accommodation")) {
                for (Product p : Main.connectionHandler.accomodation) {
                    if (((ProductAccommodation) p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductAccommodation) p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())) {
                        ((ProductAccommodation) p).setDateSelected(date);
                        products.add(p);
                    }
                }
            } else if (category.matches("Golf")) {
                for (Product p : Main.connectionHandler.golf) {
                    if (((ProductGolf) p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductGolf) p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())) {
                        ((ProductGolf) p).setDateSelected(date);
                        products.add(p);
                    }
                }
            } else if (category.matches("Transport")) {
                for (Product p : Main.connectionHandler.transport) {
                    if (((ProductTransport) p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductTransport) p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())) {
                        ((ProductTransport) p).setDateSelected(date);
                        products.add(p);
                    }
                }
            } else if (category.matches("Activitiy")) {
                for (Product p : Main.connectionHandler.activities) {
                    if (((ProductActivity) p).getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) && ((ProductActivity) p).getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString())) {
                        ((ProductActivity) p).setDateSelected(date);
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
