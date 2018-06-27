package main;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import models.Booking;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookingsPaneController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(){

    }

    public void newQuoteButtonClick(){
        
    }

    public void newQuoteButtonClick(){

    }

    public void newQuoteButtonClick(){

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
