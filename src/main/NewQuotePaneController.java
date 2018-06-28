package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
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
    @FXML Button npBtn;
    private NewQuotePackagePaneController nqppc = null;
    private NewQuoteBespokePaneController nqbpc = null;
    private String lastPane;
    private String process;
    private boolean packagePane;
    private Booking booking;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        arrivalDP.setConverter(new StringConverter<LocalDate>(){
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
        departureDP.setConverter(new StringConverter<LocalDate>(){
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
    }

    public void initEditData(String lastPane, Booking booking){
        this.lastPane = lastPane;
        this.booking = booking;
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
        nameTxf.setText(booking.getClientName());
        contactNumberTxf.setText(booking.getContactNumber());
        emailTxf.setText(booking.getEmail());
        peopleTxf.setText(booking.getPeople());
        arrivalDP.setValue(LocalDate.parse(booking.getArrival()));
        departureDP.setValue(LocalDate.parse(booking.getDeparture()));

    }

    public void initNoMailData(String lastPane, String process){
        this.lastPane = lastPane;
        this.process = process;
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
        arrivalDP.setValue(LocalDate.parse(mailMessage.getMessage().split("Date Requested: ")[1].split(" - ")[0]));
        departureDP.setValue(LocalDate.parse(mailMessage.getMessage().split("Date Requested: ")[1].split(" - ")[1].split("Adults:")[0]));
        emailFromTxa.setText("Subject: " + mailMessage.getSubject());
        emailFromTxa.appendText("Message: " + mailMessage.getMessage());
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

    public void npButtonClickClick(){
        if(packagePane){
            TripPackage selectedPackage = nqppc.getPackage();
            if(selectedPackage!=null){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("NewQuoteBespokePane.fxml"));
                HBox root = null;
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
                quoteTypeVBox.getChildren().clear();
                quoteTypeVBox.getChildren().add(root);
                packagePane = true;
            }
        }
    }

    public void previewButtonClick(){//check input /if bespoke
        if (!nameTxf.getText().matches("")) {
            if (!contactNumberTxf.getText().matches("")) {
                if (!emailTxf.getText().matches("")) {
                    if (!peopleTxf.getText().matches("")) {//TODO Adults/Children
                        if (arrivalDP.getValue() != null) {
                            if (departureDP.getValue() != null) {
                                if (booking == null) {
                                    Main.connectionHandler.outputQueue.add(new Booking(null, nameTxf.getText(), contactNumberTxf.getText(), emailTxf.getText(), peopleTxf.getText(), arrivalDP.getValue().toString(), departureDP.getValue().toString(), "Quote", Main.connectionHandler.getTotalAmountTrip(nqbpc.getCompletedPackage()).toString(), Main.connectionHandler.user.getUser().getFirstName() + " " + Main.connectionHandler.user.getUser().getLastName(), Main.connectionHandler.getFullPaymentDate(arrivalDP.getValue().toString()), 0, 0, Main.connectionHandler.getTodaysDate(), null, nqbpc.getCompletedPackage().getBookingAccommodation(), nqbpc.getCompletedPackage().getBookingGolf(), nqbpc.getCompletedPackage().getBookingActivities(), nqbpc.getCompletedPackage().getBookingTransport()));
                                } else {
                                    Main.connectionHandler.outputQueue.add(new Booking(booking.getGsNumber(), nameTxf.getText(), contactNumberTxf.getText(), emailTxf.getText(), peopleTxf.getText(), arrivalDP.getValue().toString(), departureDP.getValue().toString(), "Quote", Main.connectionHandler.getTotalAmountTrip(nqbpc.getCompletedPackage()).toString(), Main.connectionHandler.user.getUser().getFirstName() + " " + Main.connectionHandler.user.getUser().getLastName(), Main.connectionHandler.getFullPaymentDate(arrivalDP.getValue().toString()), 0, 0, Main.connectionHandler.getTodaysDate(), null, nqbpc.getCompletedPackage().getBookingAccommodation(), nqbpc.getCompletedPackage().getBookingGolf(), nqbpc.getCompletedPackage().getBookingActivities(), nqbpc.getCompletedPackage().getBookingTransport()));
                                }
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("QuotePreviewPane.fxml"));//TODO Remember last screen/Preview Screen
                                try {
                                    Main.setStage(loader.load());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                new CustomDialog(Main.stage,"Departure Date not selected", "Select Departure Date adding supplier.", new JFXButton("Ok")).showDialog();
                            }
                        } else {
                            new CustomDialog(Main.stage,"Arrival Date not entered", "Select Arrival Date before previewing quote.", new JFXButton("Ok")).showDialog();
                        }
                    } else {
                        new CustomDialog(Main.stage,"People not entered", "Enter People before  previewing quote.", new JFXButton("Ok")).showDialog();
                    }
                } else {
                    new CustomDialog(Main.stage,"Email not selected", "Enter Email before previewing quote.", new JFXButton("Ok")).showDialog();
                }
            } else {
                new CustomDialog(Main.stage,"Contact Number not selected", "Enter Contact Number before previewing quote.", new JFXButton("Ok")).showDialog();
            }
        } else {
            new CustomDialog(Main.stage,"Client Name not entered", "Enter Client Name before previewing quote.", new JFXButton("Ok")).showDialog();
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(lastPane + ".fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
