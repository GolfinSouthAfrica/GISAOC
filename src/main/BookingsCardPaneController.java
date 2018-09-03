package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Booking;


import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
    @FXML Hyperlink emailHL;
    private String process;
    private Booking booking;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Booking booking, String process){
        this.booking = booking;
        this.process = process;
        clientNameLbl.setText(booking.getClientName());
        gsNumberLbl.setText("GS" + booking.getGsNumber());
        processLbl.setText(booking.getProcess());
        arrivalDateLbl.setText(booking.getArrival());
        contactNumberLbl.setText(booking.getContactNumber());
        emailHL.setText(booking.getEmail());
        viewBtn.setTooltip(new Tooltip("View"));
        editBtn.setTooltip(new Tooltip("Edit"));
        removeBtn.setTooltip(new Tooltip("Remove"));
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
        vbpc.initData(booking, process);
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
        nqpc.initEditData("BookingsListPane", booking, process, false);
    }

    public void removeButtonClick(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Booking");
        alert.setHeaderText("Remove Booking");
        alert.setContentText("Are you sure you want to remove the booking (GS" + booking.getGsNumber() + ")?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Main.connectionHandler.outputQueue.add("rb:" + booking.getGsNumber());
        }
    }

    public void processButtonClick(){
        new BookingProcess().BookingProcess(Main.stage, booking);
    }

    public void emailHLClicked(){
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(emailHL.getText()), null);
        new CustomDialog().CustomDialog(Main.stage,"Copied", "Mail Address Copied to Clipboard.", new JFXButton("Ok"));
    }
}
