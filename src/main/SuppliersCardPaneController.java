package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Supplier;

import java.io.IOException;
import java.util.Optional;

public class SuppliersCardPaneController {

    @FXML Label supplierNameLbl;
    @FXML Label categoryLbl;
    @FXML Label provinceLbl;
    @FXML Label addressLbl;
    @FXML Button viewBtn;
    @FXML Button editBtn;
    @FXML Button removeBtn;
    private Supplier supplier;
    private String category;

    public void initData(Supplier supplier, String category){
        this.supplier = supplier;
        this.category = category;
        supplierNameLbl.setText(supplier.getSupplierName());
        categoryLbl.setText(supplier.getCategory());
        provinceLbl.setText(supplier.getProvince());
        addressLbl.setText(supplier.getAddress());
        viewBtn.setTooltip(new Tooltip("View"));
        editBtn.setTooltip(new Tooltip("Edit"));
        removeBtn.setTooltip(new Tooltip("Remove"));
    }

    public void viewButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewSuppliersPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewSuppliersPaneController vspc = loader.getController();
        vspc.initData(supplier, category);
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
        nspc.initData(supplier, "SuppliersPane", category);
    }

    public void removeButtonClick(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Package");
        alert.setHeaderText("Remove Package");
        alert.setContentText("Are you sure you want to remove the package (" + supplier.getSupplierName() + ")?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Main.connectionHandler.outputQueue.add("rs:" + supplier.getSupplierNumber());
        }
    }
}
