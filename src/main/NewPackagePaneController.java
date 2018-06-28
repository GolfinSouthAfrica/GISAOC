package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Supplier;
import models.TripPackage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewPackagePaneController implements Initializable {

    @FXML TextField packageNameTxf;
    @FXML ComboBox categoryCmb;
    @FXML ComboBox provinceCmb;
    @FXML DatePicker expityDateDP;
    @FXML ListView accommodationListView;
    @FXML ListView golfListView;
    @FXML ListView transportListView;
    @FXML ListView activitiesListView;
    private TripPackage tripPackage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryCmb.getItems().clear();
        categoryCmb.getItems().addAll("Please Select Category", "Short Break");//TODO Exc
        provinceCmb.getItems().clear();
        provinceCmb.getItems().addAll("Please Select Province", "Western Cape", "Eastern Cape", "Northern Cape", "Gauteng", "Kwa-Zulu Natal", "North West", "Mpumulanga", "Limpopo", "Free-State", "South Africa", "World");
    }

    public void initData(TripPackage tripPackage){
        this.tripPackage = tripPackage;
        packageNameTxf.setText(tripPackage.getPackageName());
        categoryCmb.getSelectionModel().select(tripPackage.getCategory());
        provinceCmb.getSelectionModel().select(tripPackage.getProvince());
        expityDateDP.setValue(LocalDate.parse(tripPackage.getExpiryDate()));
        accommodationListView.getItems().clear();
        accommodationListView.getItems().addAll(tripPackage.getBookingAccommodation());
        golfListView.getItems().clear();
        golfListView.getItems().addAll(tripPackage.getBookingGolf());
        transportListView.getItems().clear();
        transportListView.getItems().addAll(tripPackage.getBookingTransport());
        activitiesListView.getItems().clear();
        activitiesListView.getItems().addAll(tripPackage.getBookingActivities());
    }

    public void addButtonClick(){
        if (!packageNameTxf.getText().matches("")) {
            if (!categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Please Select Category")) {
                if (!provinceCmb.getSelectionModel().getSelectedItem().toString().matches("Please Select Province")) {
                    if (expityDateDP.getValue()!=null) {
                        if (tripPackage == null) {
                            Main.connectionHandler.outputQueue.add(new TripPackage(-1, packageNameTxf.getText(), -1, categoryCmb.getSelectionModel().getSelectedItem().toString(), null, provinceCmb.getSelectionModel().getSelectedItem().toString(), expityDateDP.getValue().toString(), null, accommodationListView.getItems(), golfListView.getItems(), transportListView.getItems(), activitiesListView.getItems()));//TODO People/Extra
                        } else {
                            Main.connectionHandler.outputQueue.add(new TripPackage(-1, packageNameTxf.getText(), -1, categoryCmb.getSelectionModel().getSelectedItem().toString(), null, provinceCmb.getSelectionModel().getSelectedItem().toString(), expityDateDP.getValue().toString(), null, accommodationListView.getItems(), golfListView.getItems(), transportListView.getItems(), activitiesListView.getItems()));//TODO People/Extra
                        }
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("PackagesPane.fxml"));
                        try {
                            Main.setStage(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new CustomDialog(Main.stage,"Expiry Date not selected", "Enter Address before adding package.", new JFXButton("Ok")).showDialog();
                    }
                } else {
                    new CustomDialog(Main.stage,"Category not selected", "Select Category before adding package.", new JFXButton("Ok")).showDialog();
                }
            } else {
                new CustomDialog(Main.stage,"Province not selected", "Select Province before adding package.", new JFXButton("Ok")).showDialog();
            }
        } else {
            new CustomDialog(Main.stage,"Package Name not entered", "Enter Package Name before adding package.", new JFXButton("Ok")).showDialog();
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SuppliersPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
