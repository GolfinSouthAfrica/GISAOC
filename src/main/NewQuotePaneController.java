package main;

import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import models.*;

import java.io.File;
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
    @FXML ComboBox golfersSharingCmb;
    @FXML ComboBox nonGolfersSharingCmb;
    @FXML ComboBox golfersSingleCmb;
    @FXML ComboBox nonGolfersSingleCmb;
    @FXML DatePicker arrivalDP;
    @FXML DatePicker departureDP;
    @FXML TextField packageNameTxf;
    @FXML TextArea messageTxa;
    @FXML VBox quoteTypeVBox;
    private NewQuotePackagePaneController nqppc = null;
    private NewQuoteBespokePaneController nqbpc = null;
    private String lastPane;
    private String process;
    private boolean packagePane;
    private Booking booking;
    private Boolean newBookingGenAndNotSentOrSave = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        arrivalDP.setConverter(new StringConverter<LocalDate>(){
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
        departureDP.setConverter(new StringConverter<LocalDate>(){
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
        golfersSharingCmb.getItems().clear();
        nonGolfersSharingCmb.getItems().clear();
        golfersSingleCmb.getItems().clear();
        nonGolfersSingleCmb.getItems().clear();
        for(int i = 0; i < 501; i++) {
            golfersSharingCmb.getItems().add(i);
            nonGolfersSharingCmb.getItems().add(i);
            golfersSingleCmb.getItems().add(i);
            nonGolfersSingleCmb.getItems().add(i);
        }
        golfersSharingCmb.getSelectionModel().select(0);
        nonGolfersSharingCmb.getSelectionModel().select(0);
        golfersSingleCmb.getSelectionModel().select(0);
        nonGolfersSingleCmb.getSelectionModel().select(0);
    }

    public void initEditData(String lastPane, Booking booking, boolean newBookingGenAndNotSentOrSave){
        this.newBookingGenAndNotSentOrSave = newBookingGenAndNotSentOrSave;
        this.lastPane = lastPane;
        this.booking = booking;
        packagePane = true;
        emailFromVBox.setVisible(false);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuoteBespokePane.fxml"));
        HBox root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nqbpc = loader.getController();
        nqbpc.editData(booking);
        quoteTypeVBox.getChildren().clear();
        quoteTypeVBox.getChildren().add(root);
        packagePane = false;
        nqbpc = loader.getController();
        quoteTypeVBox.getChildren().clear();
        quoteTypeVBox.getChildren().add(root);
        nameTxf.setText(booking.getClientName());
        contactNumberTxf.setText(booking.getContactNumber());
        emailTxf.setText(booking.getEmail());
        golfersSharingCmb.getSelectionModel().select(booking.getGolfersSharing());
        nonGolfersSharingCmb.getSelectionModel().select(booking.getNongolfersSharing());
        golfersSingleCmb.getSelectionModel().select(booking.getGolfersSingle());
        nonGolfersSingleCmb.getSelectionModel().select(booking.getNongolfersSingle());
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
        //TODO Get from Mail when website updated
        arrivalDP.setValue(LocalDate.parse(mailMessage.getMessage().split("Date Requested: ")[1].split(" - ")[0]));
        departureDP.setValue(LocalDate.parse(mailMessage.getMessage().split("Date Requested: ")[1].split(" - ")[1].split("Adults:")[0]));
        emailFromTxa.setText("Subject: " + mailMessage.getSubject());
        emailFromTxa.appendText("Message: " + mailMessage.getMessage());
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
                new CustomDialog().CustomDialog(Main.stage, "No Package Selected", "Please Select a Package First.", new JFXButton("Ok"));
            }
        } else {
            //if(UserNotification.confirmationDialog(Main.stage, "","Are you sure you want to go back to the package pane?")) {
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
            //}
        }
    }

    public void previewButtonClick(){//check input
        if (!nameTxf.getText().matches("")) {
            if (!contactNumberTxf.getText().matches("")) {
                if (!emailTxf.getText().matches("")) {
                    if (arrivalDP.getValue() != null) {
                        if (departureDP.getValue() != null) {
                            //if(!golfersSharingCmb.getSelectionModel().getSelectedItem().toString().matches("0") && !nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString().matches("0") && !golfersSingleCmb.getSelectionModel().getSelectedItem().toString().matches("0") && !nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString().matches("0")) {
                                if (booking == null) {
                                    booking = new Booking("New", nameTxf.getText(), contactNumberTxf.getText(), emailTxf.getText(), Integer.parseInt(golfersSharingCmb.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(golfersSingleCmb.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString()), arrivalDP.getValue().toString(), departureDP.getValue().toString(), "Quote", Main.connectionHandler.getTotalAmountTrip(nqbpc.getCompletedPackage()), Main.connectionHandler.user.getUser().getFirstName() + " " + Main.connectionHandler.user.getUser().getLastName(), Main.connectionHandler.getFullPaymentDate(arrivalDP.getValue().toString()), 0, 0, packageNameTxf.getText(), Main.connectionHandler.getTodaysDate(), "", nqbpc.getCompletedPackage().getBookingAccommodation(), nqbpc.getCompletedPackage().getBookingGolf(), nqbpc.getCompletedPackage().getBookingActivities(), nqbpc.getCompletedPackage().getBookingTransport());
                                } else {
                                    booking = new Booking(booking.getGsNumber(), nameTxf.getText(), contactNumberTxf.getText(), emailTxf.getText(), Integer.parseInt(golfersSharingCmb.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(golfersSingleCmb.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString()), arrivalDP.getValue().toString(), departureDP.getValue().toString(), booking.getProcess(), Main.connectionHandler.getTotalAmountTrip(nqbpc.getCompletedPackage()), booking.getConsultant(), Main.connectionHandler.getFullPaymentDate(arrivalDP.getValue().toString()), booking.getDepositPaid(), booking.getFullPaid(), packageNameTxf.getText(), booking.getBookingMadeDate(), booking.getNotes(), nqbpc.getCompletedPackage().getBookingAccommodation(), nqbpc.getCompletedPackage().getBookingGolf(), nqbpc.getCompletedPackage().getBookingActivities(), nqbpc.getCompletedPackage().getBookingTransport());
                                }
                                Main.connectionHandler.outputQueue.add(booking);
                                String result = Main.connectionHandler.getGSNumber();
                                String gsNumber = result.split(":")[0];
                                int length = Integer.parseInt(result.split(":")[1]);
                                booking.setGsNumber(gsNumber);
                                DataFile costing = new DataFile("Quote", "GS" + gsNumber, ".xls", length);
                                ConnectionHandler.FileDownloader fileDownloader = Main.connectionHandler.new FileDownloader(costing);
                                fileDownloader.start();
                                costing.setFileDownloader(fileDownloader);
                                Main.connectionHandler.user.update();
                                fileDownloader.done.addListener((InvalidationListener) ea -> {
                                File openFile = new File(Main.LOCAL_CACHE.getAbsolutePath() + "/" + costing.getFileType() + "/" + costing.getFileName() + ".xls");
                                    if (openFile.exists() && openFile.length() == costing.getFileLength()) {
                                        try {
                                            java.awt.Desktop.getDesktop().open(openFile);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });
                                final FXMLLoader[] loader = {new FXMLLoader()};
                                Main.quoteDone.addListener((InvalidationListener) e -> {
                                    if(Main.quoteDone.getValue()) {
                                        loader[0] = new FXMLLoader();
                                        loader[0].setLocation(getClass().getResource(lastPane + ".fxml"));
                                        try {
                                            Main.setStage(loader[0].load());
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        Main.quoteDone.setValue(false);
                                    }
                                });
                                new QuotePreviewPane(Main.stage, booking, messageTxa.getText(), length);
                            //}
                        } else {
                            new CustomDialog().CustomDialog(Main.stage,"Departure Date not selected", "Select Departure Date adding supplier.", new JFXButton("Ok"));
                        }
                    } else {
                        new CustomDialog().CustomDialog(Main.stage,"Arrival Date not entered", "Select Arrival Date before previewing quote.", new JFXButton("Ok"));
                    }
                } else {
                    new CustomDialog().CustomDialog(Main.stage,"Email not selected", "Enter Email before previewing quote.", new JFXButton("Ok"));
                }
            } else {
                new CustomDialog().CustomDialog(Main.stage,"Contact Number not selected", "Enter Contact Number before previewing quote.", new JFXButton("Ok"));
            }
        } else {
            new CustomDialog().CustomDialog(Main.stage,"Client Name not entered", "Enter Client Name before previewing quote.", new JFXButton("Ok"));
        }
    }

    public void backButtonClick(){
        if(!newBookingGenAndNotSentOrSave) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(lastPane + ".fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO Confirmation and delete to server
        }
    }
}
