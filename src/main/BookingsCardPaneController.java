package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import models.Booking;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookingsCardPaneController implements Initializable {

    @FXML Label clientNameLbl;
    @FXML Label gsNumberLbl;
    @FXML Label processLbl;
    @FXML Label arrivalDateLbl;
    @FXML Label contactNumberLbl;
    @FXML Button viewBtn;
    @FXML Button editBtn;
    @FXML Button removeBtn;
    @FXML Button processBtn;
    @FXML Button mailsBtn;
    @FXML Hyperlink emailHL;

    private Booking booking;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Booking booking){
        this.booking = booking;
        clientNameLbl.setText(booking.getClientName());
        gsNumberLbl.setText(booking.getGsNumber());
        processLbl.setText(booking.getProcess());
        arrivalDateLbl.setText(booking.getArrival());
        contactNumberLbl.setText(booking.getContactNumber());
        emailHL.setText(booking.getEmail());
        viewBtn.setTooltip(new Tooltip("View"));
        editBtn.setTooltip(new Tooltip("Edit"));
        removeBtn.setTooltip(new Tooltip("Remove"));
        processBtn.setTooltip(new Tooltip("Process"));
        mailsBtn.setTooltip(new Tooltip("Mails"));
    }

    public void viewButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewBookingPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewBookingPaneController vbpc = loader.getController();
        vbpc.initData(booking);
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
        nqpc.initEditData("BookingsPane", booking);
    }

    public void removeButtonClick(){
        Main.connectionHandler.outputQueue.add("rbk:" + booking.getGsNumber());
    }

    public void processButtonClick(){//TODO

    }

    public void mailsButtonClick(){//TODO

    }
}
