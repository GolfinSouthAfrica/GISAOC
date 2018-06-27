package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import models.Booking;
import models.TripPackage;

import java.net.URL;
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
        this.selectedPackage = selectedPackage;
        accommodationListView.getItems().clear();
        accommodationListView.getItems().addAll(this.selectedPackage.getBookingAccommodation());
        golfListView.getItems().clear();
        golfListView.getItems().addAll(this.selectedPackage.getBookingGolf());
        activitiesListView.getItems().clear();
        activitiesListView.getItems().addAll(this.selectedPackage.getBookingActivities());
        transportListView.getItems().clear();
        transportListView.getItems().addAll(this.selectedPackage.getBookingTransport());
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

    }

    public void accommodationEditButtonClick(){

    }

    public void accommodationRemoveButtonClick(){

    }

    public void golfAddButtonClick(){

    }

    public void golfEditButtonClick(){

    }

    public void golfRemoveButtonClick(){

    }

    public void activityAddButtonClick(){

    }

    public void activityEditButtonClick(){

    }

    public void activityRemoveButtonClick(){

    }

    public void tranportAddButtonClick(){

    }

    public void tranportEditButtonClick(){

    }

    public void transportRemoveButtonClick(){

    }

    public TripPackage getCompletedPackage(){
        return selectedPackage;
    }
}
