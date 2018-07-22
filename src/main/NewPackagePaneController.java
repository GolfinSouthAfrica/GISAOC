package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewPackagePaneController implements Initializable {

    @FXML TextField packageNameTxf;
    @FXML ComboBox categoryCmb;
    @FXML ComboBox provinceCmb;
    @FXML DatePicker expiryDateDP;
    @FXML ListView accommodationListView;
    @FXML ListView golfListView;
    @FXML ListView transportListView;
    @FXML ListView activitiesListView;
    private TripPackage tripPackage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expiryDateDP.setConverter(new StringConverter<LocalDate>(){
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
        categoryCmb.getItems().clear();
        categoryCmb.getItems().addAll("Please Select Category", "Short Golf Break", "Golf Tour");//TODO Exc
        categoryCmb.getSelectionModel().select(0);
        provinceCmb.getItems().clear();
        provinceCmb.getItems().addAll("Please Select Province", "Western Cape", "Eastern Cape", "Northern Cape", "Gauteng", "Kwa-Zulu Natal", "North West", "Mpumalanga", "Limpopo", "Free-State", "South Africa", "World");
        provinceCmb.getSelectionModel().select(0);
    }

    public void initData(TripPackage tripPackage){
        this.tripPackage = tripPackage;
        packageNameTxf.setText(tripPackage.getPackageName());
        categoryCmb.getSelectionModel().select(tripPackage.getCategory());
        provinceCmb.getSelectionModel().select(tripPackage.getProvince());
        expiryDateDP.setValue(LocalDate.parse(tripPackage.getExpiryDate()));
        accommodationListView.getItems().clear();
        accommodationListView.getItems().addAll(tripPackage.getBookingAccommodation());
        golfListView.getItems().clear();
        golfListView.getItems().addAll(tripPackage.getBookingGolf());
        transportListView.getItems().clear();
        transportListView.getItems().addAll(tripPackage.getBookingTransport());
        activitiesListView.getItems().clear();
        activitiesListView.getItems().addAll(tripPackage.getBookingActivities());
    }

    public void addAccommodationButtonClick(){
        BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
        Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Accommodation", null);
        accommodationListView.getItems().add(x);
    }

    public void removeAccommodationButtonClick(){
        if(accommodationListView.getSelectionModel().getSelectedItems() != null) {
            accommodationListView.getItems().remove(accommodationListView.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog(Main.stage, "No accommodation selected to remove", "Select accommodation you want to remove before clicking the remove button.", new JFXButton("Ok"));
        }
    }

    public void editAccommodationButtonClick(){
        if(accommodationListView.getSelectionModel().getSelectedItems() != null) {
            BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
            Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Accommodation", (BookingAccommodation) accommodationListView.getSelectionModel().getSelectedItem());
            accommodationListView.getItems().remove(accommodationListView.getSelectionModel().getSelectedItem());
            accommodationListView.getItems().add(x);
        } else {
            new CustomDialog(Main.stage, "No accommodation selected to edit", "Select accommodation you want to edit before clicking the edit button.", new JFXButton("Ok"));
        }
    }

    public void addGolfButtonClick(){
        BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
        Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Golf", null);
        golfListView.getItems().add(x);
    }

    public void removeGolfButtonClick(){
        if(golfListView.getSelectionModel().getSelectedItems() != null) {
            golfListView.getItems().remove(golfListView.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog(Main.stage, "No accommodation selected to remove", "Select accommodation you want to remove before clicking the remove button.", new JFXButton("Ok"));
        }
    }

    public void editGolfButtonClick(){
        if(golfListView.getSelectionModel().getSelectedItems() != null) {
            BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
            Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Golf", (BookingGolf) golfListView.getSelectionModel().getSelectedItem());
            golfListView.getItems().remove(golfListView.getSelectionModel().getSelectedItem());
            golfListView.getItems().add(x);
        } else {
            new CustomDialog(Main.stage, "No accommodation selected to edit", "Select accommodation you want to edit before clicking the edit button.", new JFXButton("Ok"));
        }
    }

    public void addTransportButtonClick(){
        BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
        Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Transport", null);
        transportListView.getItems().add(x);
    }

    public void removeTransportButtonClick(){
        if(transportListView.getSelectionModel().getSelectedItems() != null) {
            transportListView.getItems().remove(transportListView.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog(Main.stage, "No accommodation selected to remove", "Select accommodation you want to remove before clicking the remove button.", new JFXButton("Ok"));
        }
    }

    public void editTransportButtonClick(){
        if(transportListView.getSelectionModel().getSelectedItems() != null) {
            BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
            Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Transport", (BookingTransport) transportListView.getSelectionModel().getSelectedItem());
            transportListView.getItems().remove(transportListView.getSelectionModel().getSelectedItem());
            transportListView.getItems().add(x);
        } else {
            new CustomDialog(Main.stage, "No accommodation selected to edit", "Select accommodation you want to edit before clicking the edit button.", new JFXButton("Ok"));
        }
    }

    public void addActivitiesButtonClick(){
        BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
        Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Activity", null);
        activitiesListView.getItems().add(x);
    }

    public void removeActivitiesButtonClick(){
        if(activitiesListView.getSelectionModel().getSelectedItems() != null) {
            activitiesListView.getItems().remove(activitiesListView.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog(Main.stage, "No accommodation selected to remove", "Select accommodation you want to remove before clicking the remove button.", new JFXButton("Ok"));
        }
    }

    public void editActivitiesButtonClick(){
        if(activitiesListView.getSelectionModel().getSelectedItems() != null) {
            BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
            Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Activity", (BookingActivity) activitiesListView.getSelectionModel().getSelectedItem());
            activitiesListView.getItems().remove(activitiesListView.getSelectionModel().getSelectedItem());
            activitiesListView.getItems().add(x);
        } else {
            new CustomDialog(Main.stage, "No activity selected to edit", "Select activity you want to edit before clicking the edit button.", new JFXButton("Ok"));
        }
    }

    public void addButtonClick(){
        List<BookingAccommodation> accommodation = new ArrayList<>();
        for(Object p: accommodationListView.getItems()){
            if(p instanceof BookingAccommodation){
                accommodation.add((BookingAccommodation)p);
            }
        }
        List<BookingGolf> golf = new ArrayList<>();
        for(Object p: golfListView.getItems()){
            if(p instanceof BookingGolf){
                golf.add((BookingGolf)p);
            }
        }
        List<BookingActivity> activities = new ArrayList<>();
        for(Object p: activitiesListView.getItems()){
            if(p instanceof BookingActivity){
                activities.add((BookingActivity)p);
            }
        }
        List<BookingTransport> transport = new ArrayList<>();
        for(Object p: transportListView.getItems()){
            if(p instanceof BookingTransport){
                transport.add((BookingTransport)p);
            }
        }
        if (!packageNameTxf.getText().matches("")) {
            if (!categoryCmb.getSelectionModel().getSelectedItem().toString().matches("Please Select Category")) {
                if (!provinceCmb.getSelectionModel().getSelectedItem().toString().matches("Please Select Province")) {
                    if (expiryDateDP.getValue()!=null) {
                        if (tripPackage == null) {
                            TripPackage tp = new TripPackage(-1, packageNameTxf.getText(), -1, categoryCmb.getSelectionModel().getSelectedItem().toString(), 0, 0, 0, 0, provinceCmb.getSelectionModel().getSelectedItem().toString(), expiryDateDP.getValue().toString(), accommodation, golf, transport, activities);
                            tp.setTotalPackageAmount(Main.connectionHandler.getTotalAmountTrip(tp));
                            Main.connectionHandler.outputQueue.add(tp);
                        } else {
                            TripPackage tp = new TripPackage(tripPackage.getPackageID(), packageNameTxf.getText(), -1, categoryCmb.getSelectionModel().getSelectedItem().toString(), 0, 0, 0, 0, provinceCmb.getSelectionModel().getSelectedItem().toString(), expiryDateDP.getValue().toString(), accommodation, golf, transport, activities);
                            tp.setTotalPackageAmount(Main.connectionHandler.getTotalAmountTrip(tp));
                            Main.connectionHandler.outputQueue.add(tp);
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
        loader.setLocation(getClass().getResource("PackagesPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
