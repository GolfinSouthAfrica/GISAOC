package main;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import models.*;

public class BookingProcess extends CustomDialogSkin {

    Booking booking;

    public void BookingProcess(Window parent, Booking booking) {
        this.booking = booking;
        initOwner(parent);
        Text headingText = new Text("Booking Process:");
        headingText.getStyleClass().add("secondaryHeadingText");
        Text currentProcess = new Text("Current Process: " + booking.getProcess());
        Button nextProcess = new Button("Next");
        nextProcess.setOnAction(actionEvent -> {
            if(this.booking.getProcess().matches("Quote")){
                this.booking = new BookingProvisionalMade().BookingProvisionalMade(Main.stage, this.booking);
                boolean all = true;
                for(BookingAccommodation ba:this.booking.getBookingAccommodation()){
                    if(ba.getSupplierBooked() == 0){
                        all = false;
                    }
                }
                for(BookingGolf ba:this.booking.getBookingGolf()){
                    if(ba.getSupplierBooked() == 0){
                        all = false;
                    }
                }
                for(BookingTransport ba:this.booking.getBookingTransport()){
                    if(ba.getSupplierBooked() == 0){
                        all = false;
                    }
                }
                for(BookingActivity ba:this.booking.getBookingActivities()){
                    if(ba.getSupplierBooked() == 0){
                        all = false;
                    }
                }
                if(all) {
                    Main.connectionHandler.outputQueue.add("bpn:" + booking.getGsNumber() + ":PendingBookingMade:" + booking.getArrival() + ":" + booking.getClientName());
                    currentProcess.setText("Current Process: PendingBookingMade");
                    this.booking.setProcess("PendingBookingMade");
                }
            } else if(this.booking.getProcess().matches("PendingBookingMade")){
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and add the client transaction to take booking to next process.", new JFXButton("Ok"));
            } else if(this.booking.getProcess().matches("PendingDepositRecieved")) {
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and add the client transaction to take booking to next process.", new JFXButton("Ok"));
            } else if(this.booking.getProcess().matches("PendingDepositPaid")){
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and add the client transaction to take booking to next process.", new JFXButton("Ok"));
            } else if(this.booking.getProcess().matches("PendingFullRecieved")){
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and add the client transaction to take booking to next process.", new JFXButton("Ok"));
            } else if(this.booking.getProcess().matches("ConfirmedFullPaid")){
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and add the client transaction to take booking to next process.", new JFXButton("Ok"));
            }
        });
        Button cancelBooking = new Button("Previous");
        cancelBooking.setOnAction(actionEvent -> {
            if(this.booking.getProcess().matches("Quote")){
                new CustomDialog().CustomDialog(Main.stage, "No Booking Made", "No Bookings Have Been Made.", new JFXButton("Ok"));
            } else if(this.booking.getProcess().matches("PendingBookingMade")){
                Main.connectionHandler.outputQueue.add("bpp:" + booking.getGsNumber() + ":Quote:" + booking.getArrival() + ":" + booking.getClientName());
                currentProcess.setText("Current Process: Quote");
                this.booking.setProcess("Quote");
            } else if(this.booking.getProcess().matches("PendingDepositRecieved")) {
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and remove the client transactions to cancel bookings.", new JFXButton("Ok"));
            } else if(this.booking.getProcess().matches("PendingDepositPaid")){
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and remove the client transactions to cancel bookings.", new JFXButton("Ok"));
            } else if(this.booking.getProcess().matches("PendingFullRecieved")){
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and remove the client transactions to cancel bookings.", new JFXButton("Ok"));
            } else if(this.booking.getProcess().matches("ConfirmedFullPaid")){
                new CustomDialog().CustomDialog(Main.stage, "Go to the Finance Pane", "Go to the Finance Pane and remove the client transactions to cancel bookings.", new JFXButton("Ok"));
            }
        });
        Button done = new Button("Done");
        done.setOnAction(actionEvent -> {
            this.closeAnimation();
        });
        VBox bookingProcess = new VBox(headingText, currentProcess, nextProcess, cancelBooking, done);
        bookingProcess.getChildren().addAll();
        bookingProcess.setSpacing(15);
        bookingProcess.setPadding(new Insets(20));
        bookingProcess.setAlignment(Pos.TOP_CENTER);
        bookingProcess.setStyle("-fx-background-color: #595959;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;");
        bookingProcess.setMaxSize(350, 350);
        bookingProcess.setMinSize(250, 250);
        VBox settingsPane = new VBox(bookingProcess);
        setWidth(450);
        settingsPane.setAlignment(Pos.CENTER);
        getDialogPane().setContent(settingsPane);
        showDialog();
    }
}
