package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Booking;
import models.Mail;
import models.Supplier;
import models.TripPackage;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class NewQuotePaneController implements Initializable{

    @FXML Label emailFromLbl;
    @FXML TextArea emailFromTxa;
    @FXML VBox emailFromVBox;
    @FXML TextField nameTxf;
    @FXML TextField contactNumberTxf;
    @FXML TextField emailTxf;
    @FXML TextField peopleTxf;
    @FXML DatePicker arrivalDP;
    @FXML DatePicker departureDP;
    @FXML TextArea messageTxa;
    @FXML VBox quoteTypeVBox;
    private NewQuotePackagePaneController nqppc;
    private NewQuoteBespokePaneController nqbpc;
    private String lastPane;

    private boolean packagePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initNoMailData(String lastPane){
        this.lastPane = lastPane;
        packagePane = true;
        emailFromVBox.setVisible(false);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuotePackagePane.fxml"));
        VBox root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nqppc = loader.getController();
        quoteTypeVBox.getChildren().clear();
        quoteTypeVBox.getChildren().add(root);
    }

    public void initData(Mail mailMessage, String lastPane){
        this.lastPane = lastPane;
        emailFromVBox.setVisible(true);
        nameTxf.setText(mailMessage.getMessage().split("Telephone")[0].split("Name: ")[1]);
        contactNumberTxf.setText(mailMessage.getMessage().split("Email")[0].split("Telephone: ")[1]);
        emailTxf.setText(mailMessage.getMessage().split("Mailing List")[0].split("Email: ")[1]);
        peopleTxf.setText("Adults: " + mailMessage.getMessage().split("Children")[0].split("Adults: ")[1] + "Children: " + mailMessage.getMessage().split("Transport")[0].split("Children: ")[1]);
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        arrivalDP.setValue(LocalDate.parse(dateString, formatter));*///TODO
    }

    public void editBooking(Booking booking){
        emailFromVBox.setVisible(true);
        nameTxf.setText(booking.getClientName());
        contactNumberTxf.setText(booking.getContactNumber());
        emailTxf.setText(booking.getEmail());
        peopleTxf.setText(booking.getPeople());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuoteBespokePane.fxml"));
        VBox root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nqbpc = loader.getController();
        nqbpc.editData(booking);
        quoteTypeVBox.getChildren().clear();
        quoteTypeVBox.getChildren().add(root);
    }

    public void packageSelectDeselectVuttonClick(){
        if(packagePane){
            TripPackage selectedPackage = nqppc.getPackage();
            if(selectedPackage!=null){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("NewQuoteBespokePane.fxml"));
                VBox root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nqbpc = loader.getController();
                nqbpc.initData(selectedPackage);
                quoteTypeVBox.getChildren().clear();
                quoteTypeVBox.getChildren().add(root);
                packagePane = false;
            } else {
                UserNotification.showErrorMessage("No Package Selected", "Please Select a Package First.");
            }
        } else {
            if(UserNotification.confirmationDialog(Main.stage, "","Are you sure you want to go back to the package pane?")) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("NewQuotePackagePane.fxml"));
                VBox root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nqppc = loader.getController();
                quoteTypeVBox.getChildren().clear();
                quoteTypeVBox.getChildren().add(root);
                packagePane = true;
            }
        }
    }

    public void previewButtonClick(){//check input /if bespoke
        Main.connectionHandler.outputQueue.add(new Booking(null, nameTxf.getText(), contactNumberTxf.getText(), emailTxf.getText(), peopleTxf.getText(), arrivalDP.getValue().toString(), departureDP.getValue().toString(), "Quote", Main.connectionHandler.getTotalAmountTrip(nqbpc.getCompletedPackage()).toString(), Main.connectionHandler.user.getUser().getFirstName() + " " + Main.connectionHandler.user.getUser().getLastName(), Main.connectionHandler.getFullPaymentDate(arrivalDP.getValue().toString()), 0, 0, Main.connectionHandler.getTodaysDate(), null, nqbpc.getCompletedPackage().getBookingAccommodation(), nqbpc.getCompletedPackage().getBookingGolf(), nqbpc.getCompletedPackage().getBookingActivities(), nqbpc.getCompletedPackage().getBookingTransport()));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("QuotePreviewPane.fxml"));//TODO Remember last screen
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuotePane.fxml"));//TODO Remember last screen
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
