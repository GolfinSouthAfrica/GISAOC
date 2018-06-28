package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.Booking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookingsPaneController implements Initializable {

    @FXML TextField searchTxf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(){

    }

    public void searchButtonClick(){//TODO
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookingsListPaneController blpc = loader.getController();
        blpc.initData("Quotes");*/
    }

    public void quotesButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookingsListPaneController blpc = loader.getController();
        blpc.initData("Quotes");
    }

    public void pendingButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookingsListPaneController blpc = loader.getController();
        blpc.initData("Pending");
    }

    public void confirmedButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BookingsListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookingsListPaneController blpc = loader.getController();
        blpc.initData("Confirmed");
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
