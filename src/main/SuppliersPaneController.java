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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class SuppliersPaneController implements Initializable{

    @FXML private TextField searchTxf;
    @FXML private ScrollPane suppliersScrollPane;
    @FXML private VBox suppliersList;
    @FXML private ComboBox sortBy;
    String category = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        category = "all";
        sortBy.getItems().clear();
        sortBy.getItems().addAll("Unsorted", "Name-Ascend", "Name-Descend", "Province-Ascend", "Province-Descend", "Category-Ascend", "Category-Descend");
        sortBy.getSelectionModel().select(0);
        sortBy.valueProperty().addListener((obs, oldItem, newItem) -> {
            populateSuppliers(sortBy.getSelectionModel().getSelectedItem().toString(), category);
        });
        populateSuppliers(sortBy.getSelectionModel().getSelectedItem().toString(), category);
        Main.connectionHandler.supplieraccommodation.addListener((InvalidationListener) e -> {
            populateSuppliers("", category);
            sortBy.getSelectionModel().select(0);
        });
        Main.connectionHandler.suppliergolf.addListener((InvalidationListener) e -> {
            populateSuppliers("", category);
            sortBy.getSelectionModel().select(0);
        });
        Main.connectionHandler.suppliertransport.addListener((InvalidationListener) e -> {
            populateSuppliers("", category);
            sortBy.getSelectionModel().select(0);
        });
        Main.connectionHandler.supplieractivities.addListener((InvalidationListener) e -> {
            populateSuppliers("", category);
            sortBy.getSelectionModel().select(0);
        });
    }

    public void initData(String category){
        this.category = category;
        sortBy.getItems().clear();
        sortBy.getItems().addAll("Unsorted", "Name-Ascend", "Name-Descend", "Province-Ascend", "Province-Descend", "Category-Ascend", "Category-Descend");
        sortBy.getSelectionModel().select(0);
        sortBy.valueProperty().addListener((obs, oldItem, newItem) -> {
            populateSuppliers(sortBy.getSelectionModel().getSelectedItem().toString(), category);
        });
        populateSuppliers(sortBy.getSelectionModel().getSelectedItem().toString(), category);
        Main.connectionHandler.supplieraccommodation.addListener((InvalidationListener) e -> {
            populateSuppliers("", category);
            sortBy.getSelectionModel().select(0);
        });
        Main.connectionHandler.suppliergolf.addListener((InvalidationListener) e -> {
            populateSuppliers("", category);
            sortBy.getSelectionModel().select(0);
        });
        Main.connectionHandler.suppliertransport.addListener((InvalidationListener) e -> {
            populateSuppliers("", category);
            sortBy.getSelectionModel().select(0);
        });
        Main.connectionHandler.supplieractivities.addListener((InvalidationListener) e -> {
            populateSuppliers("", category);
            sortBy.getSelectionModel().select(0);
        });
    }


    private void populateSuppliers(String sort, String category){
        ObservableList<HBox> supplierCards = FXCollections.observableArrayList();
        List<Supplier> temp = new ArrayList<>();
        if(category.matches("Accommodation")) {
            temp.addAll(Main.connectionHandler.supplieraccommodation);
        } else if(category.matches("Golf")) {
            temp.addAll(Main.connectionHandler.suppliergolf);
        } else if(category.matches("Transport")) {
            temp.addAll(Main.connectionHandler.suppliertransport);
        } else if(category.matches("Activities")) {
            temp.addAll(Main.connectionHandler.supplieractivities);
            }
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
        for (Supplier s : temp) {
            FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("SuppliersCardPane.fxml"));
                HBox root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SuppliersCardPaneController scc = loader.getController();
                scc.initData(s, category);
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
            if(category.matches("Accommodation")) {
                for (Supplier s : Main.connectionHandler.supplieraccommodation) {
                    if (s.getSupplierName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getProvince().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getCategory().toLowerCase().contains(searchTxf.getText().toLowerCase()) || s.getAddress().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                        displayList.add(s);
                    }
                }
            }
        } else {
            if(category.matches("Accommodation")) {
                for (Supplier s: Main.connectionHandler.supplieraccommodation) {
                    displayList.add(s);
                }
            } else if(category.matches("Golf")) {
                for (Supplier s: Main.connectionHandler.suppliergolf) {
                    displayList.add(s);
                }
            } else if(category.matches("Transport")) {
                for (Supplier s: Main.connectionHandler.suppliertransport) {
                    displayList.add(s);
                }
            } else if(category.matches("Activities")) {
                for (Supplier s: Main.connectionHandler.supplieractivities) {
                    displayList.add(s);
                }
            }
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
            scc.initData(s, category);
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
        loader.setLocation(getClass().getResource("SuppliersSelectionPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
