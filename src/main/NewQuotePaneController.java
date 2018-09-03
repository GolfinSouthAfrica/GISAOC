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

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewQuotePaneController implements Initializable{

    @FXML TextArea notesTxa;
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
    @FXML VBox quoteTypeVBox;
    @FXML Label golfersSharingActualPriceLbl;
    @FXML TextField golfersSharingAskingPriceTxf;
    @FXML Label nonGolfersSharingActualPriceLbl;
    @FXML TextField nonGolfersSharingAskingPriceTxf;
    @FXML Label golfersSingleActualPriceLbl;
    @FXML TextField golfersSingleAskingPriceTxf;
    @FXML Label nonGolfersSingleActualPriceLbl;
    @FXML TextField nonGolfersSingleAskingPriceTxf;
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
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");
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
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");
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
        golfersSharingCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            setPerPerson();
        });
        nonGolfersSharingCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            setPerPerson();
        });
        golfersSingleCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            setPerPerson();
        });
        nonGolfersSingleCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            setPerPerson();
        });
        golfersSharingActualPriceLbl.setText("0");
        golfersSharingAskingPriceTxf.setText("0");
        nonGolfersSharingActualPriceLbl.setText("0");
        nonGolfersSharingAskingPriceTxf.setText("0");
        golfersSingleActualPriceLbl.setText("0");
        golfersSingleAskingPriceTxf.setText("0");
        nonGolfersSingleActualPriceLbl.setText("0");
        nonGolfersSingleAskingPriceTxf.setText("0");
        nameTxf.requestFocus();
    }

    public void initEditData(String lastPane, Booking booking, String process, boolean newBookingGenAndNotSentOrSave){
        this.newBookingGenAndNotSentOrSave = newBookingGenAndNotSentOrSave;
        this.lastPane = lastPane;
        this.booking = booking;
        this.process = process;
        packagePane = true;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuoteBespokePane.fxml"));
        HBox root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nqbpc = loader.getController();
        nqbpc.editData(booking, this);
        quoteTypeVBox.getChildren().clear();
        quoteTypeVBox.getChildren().add(root);
        packagePane = false;
        nqbpc = loader.getController();
        quoteTypeVBox.getChildren().clear();
        quoteTypeVBox.getChildren().add(root);
        nameTxf.setText(booking.getClientName());
        contactNumberTxf.setText(booking.getContactNumber());
        emailTxf.setText(booking.getEmail());
        packageNameTxf.setText(booking.getPackageName());
        golfersSharingCmb.getSelectionModel().select(booking.getGolfersSharing());
        nonGolfersSharingCmb.getSelectionModel().select(booking.getNongolfersSharing());
        golfersSingleCmb.getSelectionModel().select(booking.getGolfersSingle());
        nonGolfersSingleCmb.getSelectionModel().select(booking.getNongolfersSingle());
        arrivalDP.setValue(LocalDate.parse(booking.getArrival()));
        departureDP.setValue(LocalDate.parse(booking.getDeparture()));
        notesTxa.setText(booking.getNotes());
        setPerPerson();
        nameTxf.requestFocus();
    }

    public void initNoMailData(String lastPane, String process){
        this.lastPane = lastPane;
        this.process = process;
        packagePane = true;
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
        nameTxf.requestFocus();
    }

    public void initNoMailData(String lastPane){
        this.lastPane = lastPane;
        packagePane = true;
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
        nameTxf.requestFocus();
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
                nqbpc.initData(selectedPackage, this);
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
                            if(!golfersSharingCmb.getSelectionModel().getSelectedItem().toString().matches("0") || !nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString().matches("0") || !golfersSingleCmb.getSelectionModel().getSelectedItem().toString().matches("0") || !nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString().matches("0")) {
                                if (booking == null) {
                                    booking = new Booking("New", nameTxf.getText(), contactNumberTxf.getText(), emailTxf.getText(), Integer.parseInt(golfersSharingCmb.getSelectionModel().getSelectedItem().toString()), Double.parseDouble(golfersSharingAskingPriceTxf.getText()), Integer.parseInt(nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString()), Double.parseDouble(nonGolfersSharingAskingPriceTxf.getText()), Integer.parseInt(golfersSingleCmb.getSelectionModel().getSelectedItem().toString()), Double.parseDouble(golfersSingleAskingPriceTxf.getText()), Integer.parseInt(nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString()), Double.parseDouble(nonGolfersSingleAskingPriceTxf.getText()), arrivalDP.getValue().toString(), departureDP.getValue().toString(), "Quote", getTotalAmount(), Main.connectionHandler.user.getUser().getFirstName() + " " + Main.connectionHandler.user.getUser().getLastName(), Main.connectionHandler.getFullPaymentDate(arrivalDP.getValue().toString()), 0, 0, packageNameTxf.getText(), Main.connectionHandler.getTodaysDate(), notesTxa.getText(), nqbpc.getCompletedPackage().getBookingAccommodation(), nqbpc.getCompletedPackage().getBookingGolf(), nqbpc.getCompletedPackage().getBookingActivities(), nqbpc.getCompletedPackage().getBookingTransport(), null);
                                } else {
                                    booking = new Booking(booking.getGsNumber(), nameTxf.getText(), contactNumberTxf.getText(), emailTxf.getText(), Integer.parseInt(golfersSharingCmb.getSelectionModel().getSelectedItem().toString()), Double.parseDouble(golfersSharingAskingPriceTxf.getText()), Integer.parseInt(nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString()), Double.parseDouble(nonGolfersSharingAskingPriceTxf.getText()), Integer.parseInt(golfersSingleCmb.getSelectionModel().getSelectedItem().toString()), Double.parseDouble(golfersSingleAskingPriceTxf.getText()), Integer.parseInt(nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString()), Double.parseDouble(nonGolfersSingleAskingPriceTxf.getText()), arrivalDP.getValue().toString(), departureDP.getValue().toString(), booking.getProcess(), getTotalAmount(), booking.getConsultant(), Main.connectionHandler.getFullPaymentDate(arrivalDP.getValue().toString()), booking.getDepositPaid(), booking.getFullPaid(), packageNameTxf.getText(), booking.getBookingMadeDate(), notesTxa.getText(), nqbpc.getCompletedPackage().getBookingAccommodation(), nqbpc.getCompletedPackage().getBookingGolf(), nqbpc.getCompletedPackage().getBookingActivities(), nqbpc.getCompletedPackage().getBookingTransport(), booking.getTransactions());

                                }
                                Main.connectionHandler.outputQueue.add(booking);
                                String result = Main.connectionHandler.getGSNumber();
                                String gsNumber = result.split(":")[0];
                                int length = Integer.parseInt(result.split(":")[1]);
                                System.out.println("Length: " + length);
                                booking.setGsNumber(gsNumber);
                                System.out.println("GSNumber: " + gsNumber);
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
                                        if(!lastPane.matches("BookingsListPane")) {
                                            loader[0] = new FXMLLoader();
                                            loader[0].setLocation(getClass().getResource(lastPane + ".fxml"));
                                            try {
                                                Main.setStage(loader[0].load());
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            }
                                            Main.quoteDone.setValue(false);
                                        } else {
                                            loader[0] = new FXMLLoader();
                                            loader[0].setLocation(getClass().getResource(lastPane + ".fxml"));
                                            try {
                                                Main.setStage(loader[0].load());
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            }
                                            BookingsListPaneController blpc = loader[0].getController();
                                            blpc.initData(process, "");
                                            Main.quoteDone.setValue(false);
                                        }
                                    }
                                });
                                new QuotePreviewPane(Main.stage, booking, length);
                            } else {
                                new CustomDialog().CustomDialog(Main.stage,"Amount of People not Selected", "Select Amount of People before previewing quote.", new JFXButton("Ok"));
                            }
                        } else {
                            new CustomDialog().CustomDialog(Main.stage,"Departure Date not selected", "Select Departure Date before previewing quote.", new JFXButton("Ok"));
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

    private Double getTotalAmount() {
        Double total = 0.00;
        total = total + (Double.parseDouble(golfersSharingAskingPriceTxf.getText()) * Integer.parseInt(golfersSharingCmb.getSelectionModel().getSelectedItem().toString()));
        total = total + (Double.parseDouble(nonGolfersSharingAskingPriceTxf.getText()) * Integer.parseInt(nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString()));
        total = total + (Double.parseDouble(golfersSingleAskingPriceTxf.getText()) * Integer.parseInt(golfersSingleCmb.getSelectionModel().getSelectedItem().toString()));
        total = total + (Double.parseDouble(nonGolfersSingleAskingPriceTxf.getText()) * Integer.parseInt(nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString()));
        return total;
    }

    public void setPerPerson(){
        Double[] pp = new Double[4];
        pp[0] = 0.00;
        pp[1] = 0.00;
        pp[2] = 0.00;
        pp[3] = 0.00;
        Booking temp = new Booking("", "", "", "", 0, 0, 0, 0, 0, 0, 0, 0, "", "", "", 0, "", "", 0, 0, "", "", "", nqbpc.getCompletedPackage().getBookingAccommodation(), nqbpc.getCompletedPackage().getBookingGolf(), nqbpc.getCompletedPackage().getBookingActivities(), nqbpc.getCompletedPackage().getBookingTransport(), null);
        if(temp!=null) {
            for (BookingAccommodation b : temp.getBookingAccommodation()) {
                if(b.getAddTo().matches("Golfer Sharing")) {
                    pp[0] = pp[0] + (b.getSellPricePerUnit() * b.getQuantity() * b.getNights());
                } else if (b.getAddTo().matches("Non-Golfer Sharing")) {
                    pp[1] = pp[1] + (b.getSellPricePerUnit() * b.getQuantity() * b.getNights());
                } else if (b.getAddTo().matches("Golfer and Non-Golfer Sharing")) {
                    pp[0] = pp[0] + ((b.getSellPricePerUnit() * b.getQuantity() * b.getNights())/2);
                    pp[1] = pp[1] + ((b.getSellPricePerUnit() * b.getQuantity() * b.getNights())/2);
                } else if (b.getAddTo().matches("Golfer Single")) {
                    pp[2] = pp[2] + (b.getSellPricePerUnit() * b.getQuantity() * b.getNights());
                } else if (b.getAddTo().matches("Non-Golfer Single")){
                    pp[3] = pp[3] + (b.getSellPricePerUnit() * b.getQuantity() * b.getNights());
                }
            }
            for (BookingGolf b : temp.getBookingGolf()) {
                if(b.getAddTo().matches("Golfer Sharing")) {
                    pp[0] = pp[0] + (b.getSellPricePerUnit() * b.getQuantity() * b.getRounds());
                } else if (b.getAddTo().matches("Non-Golfer Sharing")) {
                    pp[1] = pp[1] + (b.getSellPricePerUnit() * b.getQuantity() * b.getRounds());
                } else if (b.getAddTo().matches("Golfer and Non-Golfer Sharing")) {
                    pp[0] = pp[0] + ((b.getSellPricePerUnit() * b.getQuantity() * b.getRounds())/2);
                    pp[1] = pp[1] + ((b.getSellPricePerUnit() * b.getQuantity() * b.getRounds())/2);
                } else if (b.getAddTo().matches("Golfer Single")) {
                    pp[2] = pp[2] + (b.getSellPricePerUnit() * b.getQuantity() * b.getRounds());
                } else if (b.getAddTo().matches("Non-Golfer Single")){
                    pp[3] = pp[3] + (b.getSellPricePerUnit() * b.getQuantity() * b.getRounds());
                }
            }
            for (BookingTransport b : temp.getBookingTransport()) {
                if(b.getAddTo().matches("Golfer Sharing")) {
                    pp[0] = pp[0] + (b.getSellPricePerUnit() * b.getQuantity());
                } else if (b.getAddTo().matches("Non-Golfer Sharing")) {
                    pp[1] = pp[1] + (b.getSellPricePerUnit() * b.getQuantity() );
                } else if (b.getAddTo().matches("Golfer and Non-Golfer Sharing")) {
                    pp[0] = pp[0] + ((b.getSellPricePerUnit() * b.getQuantity())/2);
                    pp[1] = pp[1] + ((b.getSellPricePerUnit() * b.getQuantity())/2);
                } else if (b.getAddTo().matches("Golfer Single")) {
                    pp[2] = pp[2] + (b.getSellPricePerUnit() * b.getQuantity());
                } else if (b.getAddTo().matches("Non-Golfer Single")){
                    pp[3] = pp[3] + (b.getSellPricePerUnit() * b.getQuantity());
                }
            }
            for (BookingActivity b : temp.getBookingActivities()) {
                if(b.getAddTo().matches("Golfer Sharing")) {
                    pp[0] = pp[0] + (b.getSellPricePerUnit() * b.getQuantity());
                } else if (b.getAddTo().matches("Non-Golfer Sharing")) {
                    pp[1] = pp[1] + (b.getSellPricePerUnit() * b.getQuantity());
                } else if (b.getAddTo().matches("Golfer and Non-Golfer Sharing")) {
                    pp[0] = pp[0] + ((b.getSellPricePerUnit() * b.getQuantity())/2);
                    pp[1] = pp[1] + ((b.getSellPricePerUnit() * b.getQuantity())/2);
                } else if (b.getAddTo().matches("Golfer Single")) {
                    pp[2] = pp[2] + (b.getSellPricePerUnit() * b.getQuantity());
                } else if (b.getAddTo().matches("Non-Golfer Single")){
                    pp[3] = pp[3] + (b.getSellPricePerUnit() * b.getQuantity());
                }
            }
        }
        if(Integer.parseInt(golfersSharingCmb.getSelectionModel().getSelectedItem().toString())!=0){
            pp[0] = pp[0] / Integer.parseInt(golfersSharingCmb.getSelectionModel().getSelectedItem().toString());
            if(pp[0]%50!=0){
                pp[0] = pp[0] + (50 - pp[0]%50);
            }
            golfersSharingActualPriceLbl.setText(pp[0] + "");
            golfersSharingAskingPriceTxf.setText(pp[0] + "");
        } else {
            golfersSharingActualPriceLbl.setText("0");
            golfersSharingAskingPriceTxf.setText("0");
        }
        if(Integer.parseInt(nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString())!=0){
            pp[1] = pp[1] / Integer.parseInt(nonGolfersSharingCmb.getSelectionModel().getSelectedItem().toString());
            if(pp[1]%50!=0){
                pp[1] = pp[1] + (50 - pp[1]%50);
            }
            nonGolfersSharingActualPriceLbl.setText(pp[1] + "");
            nonGolfersSharingAskingPriceTxf.setText(pp[1] + "");
        } else {
            nonGolfersSharingActualPriceLbl.setText("0");
            nonGolfersSharingAskingPriceTxf.setText("0");
        }
        if(Integer.parseInt(golfersSingleCmb.getSelectionModel().getSelectedItem().toString())!=0){
            pp[2] = pp[2] / Integer.parseInt(golfersSingleCmb.getSelectionModel().getSelectedItem().toString());
            if(pp[2]%50!=0){
                pp[2] = pp[2] + (50 - pp[2]%50);
            }
            golfersSingleActualPriceLbl.setText(pp[2] + "");
            golfersSingleAskingPriceTxf.setText(pp[2] + "");
        } else {
            golfersSingleActualPriceLbl.setText("0");
            golfersSingleAskingPriceTxf.setText("0");
        }
        if(Integer.parseInt(nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString())!=0){
            pp[3] = pp[3] / Integer.parseInt(nonGolfersSingleCmb.getSelectionModel().getSelectedItem().toString());
            if(pp[3]%50!=0){
                pp[3] = pp[3] + (50 - pp[3]%50);
            }
            nonGolfersSingleActualPriceLbl.setText(pp[3] + "");
            nonGolfersSingleAskingPriceTxf.setText(pp[3] + "");
        } else {
            nonGolfersSingleActualPriceLbl.setText("0");
            nonGolfersSingleAskingPriceTxf.setText("0");
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
            if(lastPane.matches("BookingsListPane")){
                BookingsListPaneController blpc = loader.getController();
                blpc.initData(process, "");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quote Not Saved");
            alert.setHeaderText("Quote Not Saved");
            alert.setContentText("Are you sure you want to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("HomePane.fxml"));
                try {
                    Main.setStage(loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
