package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Supplier;

import java.io.IOException;

public class SuppliersCardPaneController {

    @FXML Label supplierNameLbl;
    @FXML Label provinceLbl;
    @FXML Label contactNameLbl;
    @FXML Label contactNumberLbl;
    @FXML Hyperlink emailLbl;
    @FXML VBox nameVBox;
    @FXML VBox contactVBox;
    @FXML HBox buttonHBox;
    @FXML Button viewBtn;
    @FXML Button editBtn;
    @FXML Button removeBtn;
    private Supplier supplier;

    public void initData(Supplier supplier, Double width){
        this.supplier = supplier;
        supplierNameLbl.setText(supplier.getSupplierName());
        provinceLbl.setText(supplier.getProvince());
        contactNameLbl.setText(supplier.getContactPerson());
        contactNumberLbl.setText(supplier.getContactNumber());
        emailLbl.setText(supplier.getEmail());
        nameVBox.setPrefWidth(width);
        contactVBox.setPrefWidth(width);
        buttonHBox.setPrefWidth(width);
        viewBtn.setTooltip(new Tooltip("View"));
        editBtn.setTooltip(new Tooltip("Edit"));;
        removeBtn.setTooltip(new Tooltip("Remove"));;
    }

    public void viewButtonClick(){//TODO
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewSupplierPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewSupplierPaneController vsc = loader.getController();
        vsc.initData(supplier);*/
    }

    public void editButtonClick(){//TODO
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddSupplierPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddSupplierPaneController asc = loader.getController();
        asc.initData(supplier);*/
    }

    public void removeButtonClick(){
        if (UserNotification.confirmationDialog(Main.stage, "Are you sure you want to remove " + supplier.getSupplierName() + "?", "This will delete all associated products of the supplier as well.")) {
            Main.connectionHandler.outputQueue.add("rs:" + supplier.getSupplierNumber());
        }
    }
}
