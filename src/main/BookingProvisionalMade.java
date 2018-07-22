package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import models.*;


public class BookingProvisionalMade extends CustomDialogSkin {

    public Booking BookingProvisionalMade(Window parent, Booking booking) {
        initOwner(parent);
        Text headingText = new Text("Select the provisional bookings that have been made:");
        headingText.getStyleClass().add("secondaryHeadingText");
        VBox suppliersBooked = new VBox(headingText);
        for (BookingAccommodation ba:booking.getBookingAccommodation()){
            Text supplierName = new Text(ba.getSupplierName());
            supplierName.getStyleClass().add("tersiaryHeadingText");
            Text productName = new Text(ba.getProductName());
            productName.getStyleClass().add("tersiaryHeadingText");
            Text quantity = new Text("Quantity: " + ba.getQuantity());
            quantity.getStyleClass().add("tersiaryHeadingText");
            Text nights = new Text("Nights: " + ba.getNights());
            nights.getStyleClass().add("tersiaryHeadingText");
            Text totalPrice = new Text("Total: R " + (ba.getCostPricePerUnit() * ba.getNights() * ba.getQuantity()));
            totalPrice.getStyleClass().add("tersiaryHeadingText");
            Button booked = new Button("Booked Now");
            if(ba.getSupplierBooked()==0){
                booked.setDisable(false);
            } else {
                booked.setDisable(true);
                booked.setTooltip(new Tooltip("Supplier is Already Booked"));
            }
            booked.setOnAction(actionEvent -> {
                Main.connectionHandler.outputQueue.add("usba:" + ba.getID() + ":1");
                ba.setSupplierBooked(1);
                booked.setDisable(true);
                booked.setTooltip(new Tooltip("Supplier is Already Booked"));
            });
            HBox top = new HBox(supplierName, productName);
            top.setAlignment(Pos.CENTER);
            top.setSpacing(10);
            HBox bottom = new HBox(quantity, nights, totalPrice);
            bottom.setAlignment(Pos.CENTER);
            bottom.setSpacing(10);
            VBox supplier = new VBox(top, bottom);
            supplier.setAlignment(Pos.CENTER);
            supplier.setSpacing(10);
            HBox complete = new HBox(supplier, booked);
            complete.setAlignment(Pos.CENTER);
            complete.setSpacing(30);
            suppliersBooked.getChildren().add(complete);
        }
        for (BookingGolf ba:booking.getBookingGolf()){
            Text supplierName = new Text(ba.getSupplierName());
            supplierName.getStyleClass().add("tersiaryHeadingText");
            Text productName = new Text(ba.getProductName());
            productName.getStyleClass().add("tersiaryHeadingText");
            Text quantity = new Text("Quantity: " + ba.getQuantity());
            quantity.getStyleClass().add("tersiaryHeadingText");
            Text nights = new Text("Rounds: " + ba.getRounds());
            nights.getStyleClass().add("tersiaryHeadingText");
            Text totalPrice = new Text("Total: R " + (ba.getCostPricePerUnit() * ba.getRounds() * ba.getQuantity()));
            totalPrice.getStyleClass().add("tersiaryHeadingText");
            Button booked = new Button("Booked Now");
            if(ba.getSupplierBooked()==0){
                booked.setDisable(false);
            } else {
                booked.setDisable(true);
                booked.setTooltip(new Tooltip("Supplier is Already Booked"));
            }
            booked.setOnAction(actionEvent -> {
                Main.connectionHandler.outputQueue.add("usbg:" + ba.getID() + ":1");
                ba.setSupplierBooked(1);
                booked.setDisable(true);
                booked.setTooltip(new Tooltip("Supplier is Already Booked"));
            });
            HBox top = new HBox(supplierName, productName);
            top.setAlignment(Pos.CENTER);
            top.setSpacing(10);
            HBox bottom = new HBox(quantity, nights, totalPrice);
            bottom.setAlignment(Pos.CENTER);
            bottom.setSpacing(10);
            VBox supplier = new VBox(top, bottom);
            supplier.setAlignment(Pos.CENTER);
            supplier.setSpacing(10);
            HBox complete = new HBox(supplier, booked);
            complete.setAlignment(Pos.CENTER);
            complete.setSpacing(30);
            suppliersBooked.getChildren().add(complete);
        }
        for (BookingTransport ba:booking.getBookingTransport()){
            Text supplierName = new Text(ba.getSupplierName());
            supplierName.getStyleClass().add("tersiaryHeadingText");
            Text productName = new Text(ba.getProductName());
            productName.getStyleClass().add("tersiaryHeadingText");
            Text quantity = new Text("Quantity: " + ba.getQuantity());
            quantity.getStyleClass().add("tersiaryHeadingText");
            Text totalPrice = new Text("Total: R " + (ba.getCostPricePerUnit() * ba.getQuantity()));
            totalPrice.getStyleClass().add("tersiaryHeadingText");
            Button booked = new Button("Booked Now");
            if(ba.getSupplierBooked()==0){
                booked.setDisable(false);
            } else {
                booked.setDisable(true);
                booked.setTooltip(new Tooltip("Supplier is Already Booked"));
            }
            booked.setOnAction(actionEvent -> {
                Main.connectionHandler.outputQueue.add("usbr:" + ba.getID() + ":1");
                ba.setSupplierBooked(1);
                booked.setDisable(true);
                booked.setTooltip(new Tooltip("Supplier is Already Booked"));
            });
            HBox top = new HBox(supplierName, productName);
            top.setAlignment(Pos.CENTER);
            top.setSpacing(10);
            HBox bottom = new HBox(quantity, totalPrice);
            bottom.setAlignment(Pos.CENTER);
            bottom.setSpacing(10);
            VBox supplier = new VBox(top, bottom);
            supplier.setAlignment(Pos.CENTER);
            supplier.setSpacing(10);
            HBox complete = new HBox(supplier, booked);
            complete.setAlignment(Pos.CENTER);
            complete.setSpacing(30);
            suppliersBooked.getChildren().add(complete);
        }
        for (BookingActivity ba:booking.getBookingActivities()){
            Text supplierName = new Text(ba.getSupplierName());
            supplierName.getStyleClass().add("tersiaryHeadingText");
            Text productName = new Text(ba.getProductName());
            productName.getStyleClass().add("tersiaryHeadingText");
            Text quantity = new Text("Quantity: " + ba.getQuantity());
            quantity.getStyleClass().add("tersiaryHeadingText");
            Text totalPrice = new Text("Total: R " + (ba.getCostPricePerUnit() * ba.getQuantity()));
            totalPrice.getStyleClass().add("tersiaryHeadingText");
            Button booked = new Button("Booked Now");
            if(ba.getSupplierBooked()==0){
                booked.setDisable(false);
            } else {
                booked.setDisable(true);
                booked.setTooltip(new Tooltip("Supplier is Already Booked"));
            }
            booked.setOnAction(actionEvent -> {
                Main.connectionHandler.outputQueue.add("usbt:" + ba.getID() + ":1");
                ba.setSupplierBooked(1);
                booked.setDisable(true);
                booked.setTooltip(new Tooltip("Supplier is Already Booked"));
            });
            HBox top = new HBox(supplierName, productName);
            top.setAlignment(Pos.CENTER);
            top.setSpacing(10);
            HBox bottom = new HBox(quantity, totalPrice);
            bottom.setAlignment(Pos.CENTER);
            bottom.setSpacing(10);
            VBox supplier = new VBox(top, bottom);
            supplier.setAlignment(Pos.CENTER);
            supplier.setSpacing(10);
            HBox complete = new HBox(supplier, booked);
            complete.setAlignment(Pos.CENTER);
            complete.setSpacing(30);
            suppliersBooked.getChildren().add(complete);
        }
        Button done = new Button("Done");
        done.setOnAction(actionEvent -> {
            this.closeAnimation();
        });
        suppliersBooked.getChildren().add(done);
        suppliersBooked.getChildren().addAll();
        suppliersBooked.setSpacing(15);
        suppliersBooked.setPadding(new Insets(20));
        suppliersBooked.setAlignment(Pos.TOP_CENTER);
        suppliersBooked.setStyle("-fx-background-color: #595959;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;");
        suppliersBooked.setMaxSize(750, 650);
        suppliersBooked.setMinSize(750, 650);
        VBox settingsPane = new VBox(suppliersBooked);
        setWidth(650);
        settingsPane.setAlignment(Pos.CENTER);
        getDialogPane().setContent(settingsPane);
        showDialog();
        return booking;
    }
}
