package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import models.Supplier;
import models.TripPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PackagesCardPaneController implements Initializable {

    @FXML Label packageNameLbl;
    @FXML Label categoryLbl;
    @FXML Label provinceLbl;
    @FXML Label totalAmountLbl;
    @FXML Button viewBtn;
    @FXML Button editBtn;
    @FXML Button removeBtn;
    private TripPackage tripPackage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(TripPackage tripPackage){
        this.tripPackage = tripPackage;
        packageNameLbl.setText(tripPackage.getPackageName());
        categoryLbl.setText(tripPackage.getCategory());
        provinceLbl.setText(tripPackage.getProvince());
        totalAmountLbl.setText("R " + tripPackage.getTotalPackageAmount());
        viewBtn.setTooltip(new Tooltip("View"));
        editBtn.setTooltip(new Tooltip("Edit"));
        removeBtn.setTooltip(new Tooltip("Remove"));
    }

    public void viewButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewPackagePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewPackagePaneController vppc = loader.getController();
        vppc.initData(tripPackage);
    }

    public void editButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewPackagePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewPackagePaneController nppc = loader.getController();
        nppc.initData(tripPackage);
    }

    public void removeButtonClick(){
        if (UserNotification.confirmationDialog(Main.stage, "Are you sure you want to remove " + tripPackage.getPackageName() + "?", "This will delete all not delete current conformed bookings of this package.")) {
            Main.connectionHandler.outputQueue.add("rs:" + tripPackage.getPackageID());
        }
    }
}
