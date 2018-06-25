package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Mail;
import models.Supplier;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class NewQuotePaneController implements Initializable{

    @FXML Label emailFromLbl;
    @FXML TextArea emailFromTxa;
    @FXML VBox emailFromVBox;
    @FXML TextField nameTxf;
    @FXML TextField contactNumberTxf;
    @FXML TextField emailTxf;
    @FXML TextField peopleTxf;
    @FXML DatePicker arrivalDP;
    @FXML DatePicker departureDP;
    @FXML TextArea messageTxa;
    @FXML VBox quoteTypeVBox;
    private NewQuotePackagePaneController nqppc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailFromLbl.setVisible(false);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuotePackagePane.fxml"));
        VBox root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nqppc = loader.getController();
        quoteTypeVBox.getChildren().clear();
        quoteTypeVBox.getChildren().add(root);
    }

    public void initData(Mail mailMessage){
        emailFromLbl.setVisible(true);
        nameTxf.setText(mailMessage.getMessage().split("Telephone")[0].split("Name: ")[1]);
        contactNumberTxf.setText(mailMessage.getMessage().split("Email")[0].split("Telephone: ")[1]);
        emailTxf.setText(mailMessage.getMessage().split("Mailing List")[0].split("Email: ")[1]);
        peopleTxf.setText("Adults: " + mailMessage.getMessage().split("Children")[0].split("Adults: ")[1] + "Children: " + mailMessage.getMessage().split("Transport")[0].split("Children: ")[1]);
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        arrivalDP.setValue(LocalDate.parse(dateString, formatter));*///TODO
    }

    public void previewButtonClick(){


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("QuotePreviewPane.fxml"));//TODO Remember last screen
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuotePane.fxml"));//TODO Remember last screen
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
