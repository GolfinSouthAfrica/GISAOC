package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Supplier;

import java.io.IOException;

public class SuppliersCardPaneController {

    @FXML Label supplierNameLbl;
    @FXML Label categoryLbl;
    @FXML Label provinceLbl;
    @FXML Label addressLbl;
    @FXML Button viewBtn;
    @FXML Button editBtn;
    @FXML Button removeBtn;
    private Supplier supplier;

    public void initData(Supplier supplier){
        this.supplier = supplier;
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
        vspc.initData(supplier);
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
        nspc.initData(supplier);
    }

    public void removeButtonClick(){
        if (UserNotification.confirmationDialog(Main.stage, "Are you sure you want to remove " + supplier.getSupplierName() + "?", "This will delete all associated products of the supplier as well.")) {
            Main.connectionHandler.outputQueue.add("rs:" + supplier.getSupplierNumber());
        }
    }
}
