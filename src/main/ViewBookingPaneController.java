package main;

import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.*;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewBookingPaneController implements Initializable {

    @FXML Label clientNameLbl;
    @FXML Label gsNumberLbl;
    @FXML Label contactNumberLbl;
    @FXML Hyperlink emailHL;
    @FXML Label golfersLbl;
    @FXML Label nongolfersLbl;
    @FXML Label arrivalLbl;
    @FXML Label departureLbl;
    @FXML Label bookingAmountLbl;
    @FXML Label processLbl;
    @FXML Label fullDepositPaidLbl;
    @FXML Label fullPaymentLbl;
    @FXML Label amountOutstandingLbl;
    @FXML Label amountToBePaidLbl;
    @FXML Label notesLbl;
    @FXML ListView bookingIncludesListView;
    @FXML ListView transactionsListView;
    private String process;
    private Booking booking;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Booking booking, String process){
        Main.connectionHandler.suppliers.addListener((InvalidationListener) e -> {
            this.process = process;
            this.booking = booking;
            clientNameLbl.setText(booking.getClientName());
            gsNumberLbl.setText("GS" + booking.getGsNumber());
            contactNumberLbl.setText(booking.getContactNumber());
            emailHL.setText(booking.getEmail());
            golfersLbl.setText("" + (booking.getGolfersSharing() + booking.getGolfersSingle()));
            nongolfersLbl.setText("" + (booking.getNongolfersSharing() + booking.getNongolfersSingle()));
            bookingAmountLbl.setText("R " + booking.getBookingAmount());
            arrivalLbl.setText(booking.getArrival());
            departureLbl.setText(booking.getDeparture());
            notesLbl.setText(booking.getNotes());
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
            amountOutstandingLbl.setText("R " + getAmountOutstanding());
            amountToBePaidLbl.setText("R " + getAmountToBePaid());
            bookingIncludesListView.getItems().clear();
            bookingIncludesListView.getItems().addAll(booking.getBookingAccommodation());
            bookingIncludesListView.getItems().addAll(booking.getBookingGolf());
            bookingIncludesListView.getItems().addAll(booking.getBookingTransport());
            bookingIncludesListView.getItems().addAll(booking.getBookingActivities());
            transactionsListView.getItems().clear();
            transactionsListView.getItems().addAll(booking.getTransactions());
        });
        this.process = process;
        this.booking = booking;
        clientNameLbl.setText(booking.getClientName());
        gsNumberLbl.setText("GS" + booking.getGsNumber());
        contactNumberLbl.setText(booking.getContactNumber());
        emailHL.setText(booking.getEmail());
        golfersLbl.setText("" + (booking.getGolfersSharing() + booking.getGolfersSingle()));
        nongolfersLbl.setText("" + (booking.getNongolfersSharing() + booking.getNongolfersSingle()));
        bookingAmountLbl.setText("R " + booking.getBookingAmount());
        arrivalLbl.setText(booking.getArrival());
        departureLbl.setText(booking.getDeparture());
        notesLbl.setText(booking.getNotes());
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
        amountOutstandingLbl.setText("R " + getAmountOutstanding());
        amountToBePaidLbl.setText("R " + getAmountToBePaid());
        bookingIncludesListView.getItems().clear();
        bookingIncludesListView.getItems().addAll(booking.getBookingAccommodation());
        bookingIncludesListView.getItems().addAll(booking.getBookingGolf());
        bookingIncludesListView.getItems().addAll(booking.getBookingTransport());
        bookingIncludesListView.getItems().addAll(booking.getBookingActivities());
        transactionsListView.getItems().clear();
        transactionsListView.getItems().addAll(booking.getTransactions());
    }

    private Double getAmountOutstanding(){
        Double amountOutstanding = booking.getBookingAmount();
        for(Transaction t:booking.getTransactions()){
            if(t.getTransactionType().matches("Money Came In")) {
                amountOutstanding = amountOutstanding - t.getAmount();
            }
        }
        return amountOutstanding;
    }

    private Double getAmountToBePaid(){
        Double amountToBePaid = getTotalSupplierInvoice();
        for(Transaction t:booking.getTransactions()){
            if(t.getTransactionType().matches("Supplier Paid") || t.getTransactionType().matches("Money Paid Out")) {
                amountToBePaid = amountToBePaid - t.getAmount();
            }
        }
        return amountToBePaid;
    }

    public Double getTotalSupplierInvoice(){
        double x = 0.0;
        for(BookingAccommodation a:booking.getBookingAccommodation()){
            x += a.getCostPricePerUnit()*a.getQuantity()*a.getNights();
        }
        for(BookingGolf a:booking.getBookingGolf()){
            x += a.getCostPricePerUnit()*a.getQuantity()*a.getRounds();
        }
        for(BookingTransport a:booking.getBookingTransport()){
            x += a.getCostPricePerUnit()*a.getQuantity();
        }
        for(BookingActivity a:booking.getBookingActivities()){
            x += a.getCostPricePerUnit()*a.getQuantity();
        }
        return x;
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
        nqpc.initEditData("ViewBookingsPane", booking, "", false);
    }

    public void processButtonClick(){
        new BookingProcess().BookingProcess(Main.stage, booking);
    }

    public void emailHLClicked(){
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(emailHL.getText()), null);
        new CustomDialog().CustomDialog(Main.stage,"Copied", "Mail Address Copied to Clipboard.", new JFXButton("Ok"));
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookingsListPaneController blpc = loader.getController();
        blpc.initData(process, "");
    }

}
