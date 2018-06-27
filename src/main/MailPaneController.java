package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MailPaneController implements Initializable{

    @FXML Button newQuoteNotificationBtn;
    @FXML Button contactNotificationBtn;
    @FXML Button financeNotificationBtn;
    @FXML Button otherNotificationBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.connectionHandler.unreadMails.size() == 4){
            newQuoteNotificationBtn.setLabel(Main.connectionHandler.unreadMails.get(0).toString());
            contactNotificationBtn.setLabel(Main.connectionHandler.unreadMails.get(1).toString());
            financeNotificationBtn.setLabel(Main.connectionHandler.unreadMails.get(2).toString());
            otherNotificationBtn.setLabel(Main.connectionHandler.unreadMails.get(3).toString());
        }
        Main.connectionHandler.unreadMails.addListener((InvalidationListener) e -> {
            if(Main.connectionHandler.unreadMails.size() == 4){
                newQuoteNotificationBtn.setLabel(Main.connectionHandler.unreadMails.get(0).toString());
                contactNotificationBtn.setLabel(Main.connectionHandler.unreadMails.get(1).toString());
                financeNotificationBtn.setLabel(Main.connectionHandler.unreadMails.get(2).toString());
                otherNotificationBtn.setLabel(Main.connectionHandler.unreadMails.get(3).toString());
            }
        });
    }

    public void quotesButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailSecondPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSecondPaneController mspc = loader.getController();
        mspc.initData("Quotes");
    }

    public void contactButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailSecondPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSecondPaneController mspc = loader.getController();
        mspc.initData("Contact");
    }

    public void financeButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailSecondPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSecondPaneController mspc = loader.getController();
        mspc.initData("Finance");
    }

    public void otherButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailSecondPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSecondPaneController mspc = loader.getController();
        mspc.initData("Other");
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
