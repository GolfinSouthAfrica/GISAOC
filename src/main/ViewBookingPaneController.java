package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.Booking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewBookingPaneController implements Initializable {

    @FXML Label clientNameLbl;
    @FXML Label gsNumberLbl;
    @FXML Label contactNumberLbl;
    @FXML Hyperlink emailHL;
    @FXML Label peopleLbl;
    @FXML Label bookingAmountLbl;
    @FXML Label arrivalLbl;
    @FXML Label departureLbl;
    @FXML Label consultantLbl;
    @FXML Label processLbl;
    @FXML Label fullDepositPaidLbl;
    @FXML Label fullPaymentLbl;
    @FXML ListView accommodationListView;
    @FXML ListView golfListView;
    @FXML ListView transportListView;
    @FXML ListView activitiesListView;

    private Booking booking;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Booking booking){
        Main.connectionHandler.suppliers.addListener((InvalidationListener) e -> {
            this.booking = booking;
            clientNameLbl.setText(booking.getClientName());
            gsNumberLbl.setText(booking.getGsNumber());
            contactNumberLbl.setText(booking.getContactNumber());
            emailHL.setText(booking.getEmail());
            peopleLbl.setText(booking.getPeople());
            bookingAmountLbl.setText(booking.getBookingAmount());
            arrivalLbl.setText(booking.getArrival());
            departureLbl.setText(booking.getDeparture());
            consultantLbl.setText(booking.getConsultant());
            processLbl.setText(booking.getProcess());
            if(booking.getDepositPaid() == 1){
                fullDepositPaidLbl.setText("Yes");
            } else {
                fullDepositPaidLbl.setText("No");
            }
            if (booking.getFullPaid() == 1){
                fullPaymentLbl.setText("Yes");
            } else {
                fullPaymentLbl.setText("No");
            }
            accommodationListView.getItems().clear();
            accommodationListView.getItems().addAll(booking.getBookingAccommodation());
            golfListView.getItems().clear();
            golfListView.getItems().addAll(booking.getBookingGolf());
            transportListView.getItems().clear();
            transportListView.getItems().addAll(booking.getBookingTransport());
            activitiesListView.getItems().clear();
            activitiesListView.getItems().addAll(booking.getBookingActivities());
        });
        this.booking = booking;
        clientNameLbl.setText(booking.getClientName());
        gsNumberLbl.setText(booking.getGsNumber());
        contactNumberLbl.setText(booking.getContactNumber());
        emailHL.setText(booking.getEmail());
        peopleLbl.setText(booking.getPeople());
        bookingAmountLbl.setText(booking.getBookingAmount());
        arrivalLbl.setText(booking.getArrival());
        departureLbl.setText(booking.getDeparture());
        consultantLbl.setText(booking.getConsultant());
        processLbl.setText(booking.getProcess());
        if(booking.getDepositPaid() == 1){
            fullDepositPaidLbl.setText("Yes");
        } else {
            fullDepositPaidLbl.setText("No");
        }
        if (booking.getFullPaid() == 1){
            fullPaymentLbl.setText("Yes");
        } else {
            fullPaymentLbl.setText("No");
        }
        accommodationListView.getItems().clear();
        accommodationListView.getItems().addAll(booking.getBookingAccommodation());
        golfListView.getItems().clear();
        golfListView.getItems().addAll(booking.getBookingGolf());
        transportListView.getItems().clear();
        transportListView.getItems().addAll(booking.getBookingTransport());
        activitiesListView.getItems().clear();
        activitiesListView.getItems().addAll(booking.getBookingActivities());

    }

    public void editButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuotePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewQuotePaneController nqpc = loader.getController();
        nqpc.initEditData("ViewBookingsPane", booking);
    }

    public void processButtonClick(){//TODO

    }

    public void mailsButtonClick(){//TODO

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
