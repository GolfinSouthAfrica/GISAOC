package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewQuoteBespokePaneController implements Initializable {

    @FXML ListView accommodationListView;
    @FXML ListView golfListView;
    @FXML ListView activitiesListView;
    @FXML ListView transportListView;
    private TripPackage selectedPackage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(TripPackage selectedPackage){
        if(!selectedPackage.getPackageName().matches("Bespoke")) {
            this.selectedPackage = selectedPackage;
            accommodationListView.getItems().clear();
            accommodationListView.getItems().addAll(this.selectedPackage.getBookingAccommodation());
            golfListView.getItems().clear();
            golfListView.getItems().addAll(this.selectedPackage.getBookingGolf());
            activitiesListView.getItems().clear();
            activitiesListView.getItems().addAll(this.selectedPackage.getBookingActivities());
            transportListView.getItems().clear();
            transportListView.getItems().addAll(this.selectedPackage.getBookingTransport());
        } else {
          this.selectedPackage = selectedPackage;
        }
    }

    public void editData(Booking booking){
        accommodationListView.getItems().clear();
        accommodationListView.getItems().addAll(booking.getBookingAccommodation());
        golfListView.getItems().clear();
        golfListView.getItems().addAll(booking.getBookingGolf());
        transportListView.getItems().clear();
        transportListView.getItems().addAll(booking.getBookingTransport());
        activitiesListView.getItems().clear();
        activitiesListView.getItems().addAll(booking.getBookingActivities());
    }

    public void accommodationAddButtonClick(){
        BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
        Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Accommodation", null);
        if(x!=null) {
            accommodationListView.getItems().add(x);
        }
    }

    public void accommodationEditButtonClick(){
        if(accommodationListView.getSelectionModel().getSelectedItems() != null) {
            BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
            Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Accommodation", (ProductAccommodation) accommodationListView.getSelectionModel().getSelectedItem());
            accommodationListView.getItems().remove(accommodationListView.getSelectionModel().getSelectedItem());
            accommodationListView.getItems().add(x);
        } else {
            new CustomDialog().CustomDialog(Main.stage, "No accommodation selected to edit", "Select accommodation you want to edit before clicking the edit button.", new JFXButton("Ok"));
        }
    }

    public void accommodationRemoveButtonClick(){
        if(accommodationListView.getSelectionModel().getSelectedItems() != null) {
            accommodationListView.getItems().remove(accommodationListView.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog().CustomDialog(Main.stage, "No accommodation selected to remove", "Select accommodation you want to remove before clicking the remove button.", new JFXButton("Ok"));
        }
    }

    public void golfAddButtonClick(){
        BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
        Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Golf", null);
        if(x!=null) {
            golfListView.getItems().add(x);
        }
    }

    public void golfEditButtonClick(){
        if(golfListView.getSelectionModel().getSelectedItems() != null) {
            BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
            Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Golf", (ProductGolf) golfListView.getSelectionModel().getSelectedItem());
            golfListView.getItems().remove(golfListView.getSelectionModel().getSelectedItem());
            golfListView.getItems().add(x);
        } else {
            new CustomDialog().CustomDialog(Main.stage, "No golf selected to edit", "Select golf you want to edit before clicking the edit button.", new JFXButton("Ok"));
        }
    }

    public void golfRemoveButtonClick(){
        if(golfListView.getSelectionModel().getSelectedItems() != null) {
            golfListView.getItems().remove(golfListView.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog().CustomDialog(Main.stage, "No golf selected to remove", "Select golf you want to remove before clicking the remove button.", new JFXButton("Ok"));
        }
    }

    public void activityAddButtonClick(){
        BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
        Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Activity", null);
        if(x!=null) {
            activitiesListView.getItems().add(x);
        }
    }

    public void activityEditButtonClick(){
        if(activitiesListView.getSelectionModel().getSelectedItems() != null) {
            BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
            Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Activity", (ProductActivity) activitiesListView.getSelectionModel().getSelectedItem());
            activitiesListView.getItems().remove(activitiesListView.getSelectionModel().getSelectedItem());
            activitiesListView.getItems().add(x);
        } else {
            new CustomDialog().CustomDialog(Main.stage, "No accommodation selected to edit", "Select accommodation you want to edit before clicking the edit button.", new JFXButton("Ok"));
        }
    }

    public void activityRemoveButtonClick(){
        if(activitiesListView.getSelectionModel().getSelectedItems() != null) {
            activitiesListView.getItems().remove(activitiesListView.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog().CustomDialog(Main.stage, "No activity selected to remove", "Select activity you want to remove before clicking the remove button.", new JFXButton("Ok"));
        }
    }

    public void tranportAddButtonClick(){
        BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
        Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Transport", null);
        if(x!=null) {
            transportListView.getItems().add(x);
        }
    }

    public void tranportEditButtonClick(){
        if(transportListView.getSelectionModel().getSelectedItems() != null) {
            BespokePackageSelectDialog bDialog = new BespokePackageSelectDialog();
            Product x = bDialog.BespokePackageSelectDialog(Main.stage, "Transport", (ProductTransport) transportListView.getSelectionModel().getSelectedItem());
            transportListView.getItems().remove(transportListView.getSelectionModel().getSelectedItem());
            transportListView.getItems().add(x);
        } else {
            new CustomDialog().CustomDialog(Main.stage, "No accommodation selected to edit", "Select accommodation you want to edit before clicking the edit button.", new JFXButton("Ok"));
        }
    }

    public void transportRemoveButtonClick(){
        if(transportListView.getSelectionModel().getSelectedItems() != null) {
            transportListView.getItems().remove(transportListView.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog().CustomDialog(Main.stage, "No transport selected to remove", "Select transport you want to remove before clicking the remove button.", new JFXButton("Ok"));
        }
    }

    public TripPackage getCompletedPackage(){
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
        selectedPackage.setBookingAccommodation(accommodation);
        selectedPackage.setBookingGolf(golf);
        selectedPackage.setBookingActivities(activities);
        selectedPackage.setBookingTransport(transport);
        return selectedPackage;
    }
}
