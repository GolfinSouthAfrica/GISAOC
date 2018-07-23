package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.Booking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookingsPaneController implements Initializable {

    @FXML TextField searchTxf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(){

    }

    public void searchButtonClick(){
        boolean found = false;
        for(Booking b:Main.connectionHandler.bookings){
            if(b.getGsNumber().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getProcess().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getClientName().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getEmail().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getArrival().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getContactNumber().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getDeparture().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getConsultant().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getBookingMadeDate().toLowerCase().contains(searchTxf.getText().toLowerCase()) || b.getPackageName().toLowerCase().contains(searchTxf.getText().toLowerCase())){
                found = true;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
                try {
                    Main.setStage(loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BookingsListPaneController blpc = loader.getController();
                if (b.getProcess().contains("Quote")) {
                    blpc.initData("Quote", searchTxf.getText());
                } else if (b.getProcess().contains("Pending")){
                    blpc.initData("Pending", searchTxf.getText());
                } else if (b.getProcess().contains("Confirmed")) {
                    blpc.initData("Confirmed", searchTxf.getText());
                }
            }
        }
        if(!found) {
            new CustomDialog().CustomDialog(Main.stage, "Not such Booking", "Could not found a booking with that data", new JFXButton("Ok"));
        }
    }

    public void quotesButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookingsListPaneController blpc = loader.getController();
        blpc.initData("Quote", "");
    }

    public void pendingButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookingsListPaneController blpc = loader.getController();
        blpc.initData("Pending", "");
    }

    public void confirmedButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookingsListPaneController blpc = loader.getController();
        blpc.initData("Confirmed", "");
}

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
