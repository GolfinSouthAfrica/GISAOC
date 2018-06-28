package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.TripPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewPackagePaneController implements Initializable {

    @FXML Label packageNameLbl;
    @FXML Label provinceLbl;
    @FXML Label categoryLbl;
    @FXML Label expiryDateLbl;
    @FXML Label totalAmountLbl;
    @FXML ListView accommodationListView;
    @FXML ListView golfListView;
    @FXML ListView transportListView;
    @FXML ListView activitiesListView;
    private TripPackage tripPackage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(TripPackage tripPackage){
        Main.connectionHandler.suppliers.addListener((InvalidationListener) e -> {
            this.tripPackage = tripPackage;
            packageNameLbl.setText(tripPackage.getPackageName());
            provinceLbl.setText(tripPackage.getProvince());
            categoryLbl.setText(tripPackage.getCategory());
            expiryDateLbl.setText(tripPackage.getExpiryDate());
            totalAmountLbl.setText("R " + tripPackage.getTotalPackageAmount());
            populateLists();
        });
        this.tripPackage = tripPackage;
        packageNameLbl.setText(tripPackage.getPackageName());
        provinceLbl.setText(tripPackage.getProvince());
        categoryLbl.setText(tripPackage.getCategory());
        expiryDateLbl.setText(tripPackage.getExpiryDate());
        totalAmountLbl.setText("R " + tripPackage.getTotalPackageAmount());
        populateLists();
    }

    private void populateLists(){
        accommodationListView.getItems().clear();
        accommodationListView.getItems().addAll(tripPackage.getBookingAccommodation());
        golfListView.getItems().clear();
        golfListView.getItems().addAll(tripPackage.getBookingGolf());
        transportListView.getItems().clear();
        transportListView.getItems().addAll(tripPackage.getBookingTransport());
        activitiesListView.getItems().clear();
        activitiesListView.getItems().addAll(tripPackage.getBookingActivities());

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

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PackagesPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
