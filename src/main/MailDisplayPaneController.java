package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import models.Booking;
import models.Mail;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MailDisplayPaneController implements Initializable {

    @FXML Label recipientsLbl;
    @FXML Label fromLbl;
    @FXML Label subjectLbl;
    @FXML Label dateLbl;
    @FXML TextArea messageTxa;
    @FXML Button viewAttachmentsBtn;
    @FXML Label readUnreadLbl;
    private String category;
    private String flag;
    private Mail mail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Mail mail, String category, String flag){
        this.mail = mail;
        this.category = category;
        this.flag = flag;
        recipientsLbl.setText("");
        for(String s:mail.getRecipients()){
            recipientsLbl.setText(recipientsLbl.getText() + "; " + s);
        }
        fromLbl.setText("");
        for(String s:mail.getFromMailAddress()){
            fromLbl.setText(fromLbl.getText() + "; " + s);
        }
        subjectLbl.setText(mail.getSubject());
        dateLbl.setText(mail.getDate());
        messageTxa.setText(mail.getMessage());
        if(mail.getAttachments().size() < 1){
            viewAttachmentsBtn.setVisible(false);
        }
        if(mail.isRead()){
            readUnreadLbl.setText("Mark Unread");
        } else {
            readUnreadLbl.setText("Mark Read");
        }
    }

    public void newQuoteButtonClick(){
        boolean found = false;
        for (Booking booking: Main.connectionHandler.bookings){
            if(booking.getClientName().matches(mail.getMessage().split("Telephone")[0].split("Name: ")[1]) && booking.getContactNumber().matches(mail.getMessage().split("Email")[0].split("Telephone: ")[1]) && booking.getEmail().matches(mail.getMessage().split("Mailing List")[0].split("Email: ")[1])){
                found = true;
            }
        }

        if(!found) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("NewQuotePane.fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            NewQuotePaneController mlpc = loader.getController();
            mlpc.initData(mail, "MailDisplayPane");
        } else {

        }
    }

    public void newMailButtonClick(){//TODO
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewMailPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "unread");*/
    }

    public void replyButtonClick(){
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newMailPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "all");*/
    }

    public void forwardButtonClick(){
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewMailPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "unread");*/
    }

    public void readUnreadButtonClick(){//TODO send server update
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "all");*/
    }

    public void deleteButtonClick(){
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "all");*/
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, flag);
    }
}
