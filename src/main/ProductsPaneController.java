package main;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductsPaneController implements Initializable {

    @FXML ComboBox categoryCmb;
    @FXML ComboBox provinceCmb;
    @FXML ComboBox supplierCmb;
    @FXML TextField searchTxf;
    @FXML ListView productsListView;
    @FXML ListView pricesListView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryCmb.getItems().clear();
        categoryCmb.getItems().addAll("Select Category", "All", "Accommodation", "Golf", "Transport", "Activities");
        categoryCmb.getSelectionModel().select("Select Category");
        categoryCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem.toString().matches("Select Category")){
                supplierCmb.getItems().clear();
                provinceCmb.getItems().clear();
            } else {
                provinceCmb.getItems().addAll("Select Province", "All", "Western Cape", "Eastern Cape", "Northern Cape", "Gauteng", "Kwa-Zulu Natal", "North West", "Mpumalanga", "Limpopo", "Free-State", "South Africa", "World");
                provinceCmb.getSelectionModel().select("Select Province");
            }
        });
        provinceCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem.toString().matches("Select Province")){
                supplierCmb.getItems().clear();
            } else {
                supplierCmb.getItems().clear();
                supplierCmb.getItems().add("Select Supplier");
                supplierCmb.getSelectionModel().select("Select Supplier");
                supplierCmb.getItems().addAll(getSuppliers());
            }
        });
        supplierCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(supplierCmb.getSelectionModel().getSelectedItem() != null){
                List<Product> products = new ArrayList<>();
                if(categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Accommodation") || categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")){
                    for (ProductAccommodation pa:Main.connectionHandler.accomodation){
                        if(pa.getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) || supplierCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                            pa.setDateSelected("All");
                            products.add(pa);
                        }
                    }
                }
                if(categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Golf") || categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    for (ProductGolf pg:Main.connectionHandler.golf){
                        if(pg.getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) || supplierCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                            pg.setDateSelected("All");
                            products.add(pg);
                        }
                    }
                }
                if(categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Transport") || categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    for (ProductTransport pt:Main.connectionHandler.transport){
                        if(pt.getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) || supplierCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                            pt.setDateSelected("All");
                            products.add(pt);
                        }
                    }
                }
                if(categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Activities") || categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    for (ProductActivity pa:Main.connectionHandler.activities){
                        if(pa.getSupplierName().matches(supplierCmb.getSelectionModel().getSelectedItem().toString()) || supplierCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
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
        });
        productsListView.setOnMouseClicked(e -> {
            if(productsListView.getSelectionModel().getSelectedItem() != null){
                if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Accommodation") || categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    ProductAccommodation p = (ProductAccommodation) productsListView.getSelectionModel().getSelectedItem();
                    pricesListView.getItems().clear();
                    for (String[] s : p.getPrices()) {
                        pricesListView.getItems().add("Valid: " + s[0] + " - " + s[1] + " Price: R " + s[2]);
                    }
                } else if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Golf") || categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    ProductGolf p = (ProductGolf) productsListView.getSelectionModel().getSelectedItem();
                    pricesListView.getItems().clear();
                    for (String[] s : p.getPrices()) {
                        pricesListView.getItems().add("Valid: " + s[0] + " - " + s[1] + " Price: R " + s[2]);
                    }
                } else if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Transport") || categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    ProductTransport p = (ProductTransport) productsListView.getSelectionModel().getSelectedItem();
                    pricesListView.getItems().clear();
                    for (String[] s : p.getPrices()) {
                        pricesListView.getItems().add("Valid: " + s[0] + " - " + s[1] + " Price: R " + s[2]);
                    }

                } else if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Activities") || categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    ProductActivity p = (ProductActivity) productsListView.getSelectionModel().getSelectedItem();
                    pricesListView.getItems().clear();
                    for (String[] s : p.getPrices()) {
                        pricesListView.getItems().add("Valid: " + s[0] + " - " + s[1] + " Price: R " + s[2]);
                    }
                }
            }
        });
    }

    private List<String>getSuppliers(){
        List<String>suppliers = new ArrayList<>();
        if(categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Accommodation")) {
            for (Supplier s : Main.connectionHandler.supplieraccommodation) {
                if(s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    suppliers.add(s.getSupplierName());
                }
            }
        } else if(categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Golf")) {
            for (Supplier s : Main.connectionHandler.suppliergolf) {
                if(s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    suppliers.add(s.getSupplierName());
                }
            }
        } else if(categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Transport")) {
            for (Supplier s : Main.connectionHandler.suppliertransport) {
                if(s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    suppliers.add(s.getSupplierName());
                }
            }
        } else if(categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Activity")) {
            for (Supplier s : Main.connectionHandler.supplieractivities) {
                if(s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    suppliers.add(s.getSupplierName());
                }
            }
        } else if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
            for (Supplier s : Main.connectionHandler.supplieraccommodation) {
                if(s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    suppliers.add(s.getSupplierName());
                }
            }
            for (Supplier s : Main.connectionHandler.suppliergolf) {
                if(s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    suppliers.add(s.getSupplierName());
                }
            }
            for (Supplier s : Main.connectionHandler.suppliertransport) {
                if(s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    suppliers.add(s.getSupplierName());
                }
            }
            for (Supplier s : Main.connectionHandler.supplieractivities) {
                if(s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                    suppliers.add(s.getSupplierName());
                }
            }
        }
        return suppliers;
    }

    public void searchButtonClick(){
        if(!categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Select Category")) {
            if(!provinceCmb.getSelectionModel().getSelectedItem().toString().matches("Select Province")) {
                supplierCmb.getItems().clear();
                ObservableList<String> suppliers = FXCollections.observableArrayList();
                if (!searchTxf.getText().matches("")) {
                    if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Accommodation")) {
                        for (Supplier s : Main.connectionHandler.supplieraccommodation) {
                            if (s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                                if(s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                                    suppliers.add(s.getSupplierName());
                                }
                            }
                        }
                    } else if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Golf")) {
                        for (Supplier s : Main.connectionHandler.suppliergolf) {
                            if (s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                                if(s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                                    suppliers.add(s.getSupplierName());
                                }
                            }
                        }
                    } else if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Transport")) {
                        for (Supplier s : Main.connectionHandler.suppliertransport) {
                            if (s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                                if(s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                                    suppliers.add(s.getSupplierName());
                                }
                            }
                        }
                    } else if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Activities")) {
                        for (Supplier s : Main.connectionHandler.supplieractivities) {
                            if (s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                                if(s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                                    suppliers.add(s.getSupplierName());
                                }
                            }
                        }
                    } else if (categoryCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                        for (Supplier s : Main.connectionHandler.supplieraccommodation) {
                            if (s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                                if(s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                                    suppliers.add(s.getSupplierName());
                                }
                            }
                        }
                        for (Supplier s : Main.connectionHandler.suppliergolf) {
                            if (s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                                if(s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                                    suppliers.add(s.getSupplierName());
                                }
                            }
                        }
                        for (Supplier s : Main.connectionHandler.suppliertransport) {
                            if (s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                                if(s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                                    suppliers.add(s.getSupplierName());
                                }
                            }
                        }
                        for (Supplier s : Main.connectionHandler.supplieractivities) {
                            if (s.getProvince().matches(provinceCmb.getSelectionModel().getSelectedItem().toString()) || provinceCmb.getSelectionModel().getSelectedItem().toString().matches("All")) {
                                if(s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                                    suppliers.add(s.getSupplierName());
                                }
                            }
                        }
                    }
                    supplierCmb.getItems().clear();
                    supplierCmb.getItems().add("Select Supplier");
                    supplierCmb.getSelectionModel().select("Select Supplier");
                    supplierCmb.getItems().addAll(suppliers);
                    productsListView.getItems().clear();
                    pricesListView.getItems().clear();
                } else {
                    supplierCmb.getItems().clear();
                    supplierCmb.getItems().add("Select Supplier");
                    supplierCmb.getSelectionModel().select("Select Supplier");
                    supplierCmb.getItems().addAll(getSuppliers());
                    productsListView.getItems().clear();
                    pricesListView.getItems().clear();
                }
            } else {
                new CustomDialog().CustomDialog(Main.stage,"Province Not Selected", "Please Select Province Before Search", new JFXButton("Ok"));
            }
        } else {
            new CustomDialog().CustomDialog(Main.stage,"Category Not Selected", "Please Select Category Before Search", new JFXButton("Ok"));
        }
    }

    public void backButtonClick() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
