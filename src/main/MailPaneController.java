package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MailPaneController implements Initializable{

    @FXML Label newQuoteNotificationBtn;
    @FXML Label contactNotificationBtn;
    @FXML Label financeNotificationBtn;
    @FXML Label otherNotificationBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newQuoteNotificationBtn.setVisible(false);
        contactNotificationBtn.setVisible(false);
        financeNotificationBtn.setVisible(false);
        otherNotificationBtn.setVisible(false);
        if(Main.connectionHandler.unreadMails.size() == 4){
            if(Main.connectionHandler.unreadMails.get(0) > 0) {
                newQuoteNotificationBtn.setVisible(true);
                newQuoteNotificationBtn.setText(Main.connectionHandler.unreadMails.get(0).toString());
            }
            if(Main.connectionHandler.unreadMails.get(1) > 0) {
                contactNotificationBtn.setVisible(true);
                contactNotificationBtn.setText(Main.connectionHandler.unreadMails.get(1).toString());
            }
            if(Main.connectionHandler.unreadMails.get(2) > 0) {
                financeNotificationBtn.setVisible(true);
                financeNotificationBtn.setText(Main.connectionHandler.unreadMails.get(2).toString());
            }
            if(Main.connectionHandler.unreadMails.get(3) > 0) {
                otherNotificationBtn.setVisible(true);
                otherNotificationBtn.setText(Main.connectionHandler.unreadMails.get(3).toString());
            }
        }
        Main.connectionHandler.unreadMails.addListener((InvalidationListener) e -> {
            if(Main.connectionHandler.unreadMails.size() == 4){
                newQuoteNotificationBtn.setText(Main.connectionHandler.unreadMails.get(0).toString());
                contactNotificationBtn.setText(Main.connectionHandler.unreadMails.get(1).toString());
                financeNotificationBtn.setText(Main.connectionHandler.unreadMails.get(2).toString());
                otherNotificationBtn.setText(Main.connectionHandler.unreadMails.get(3).toString());
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
