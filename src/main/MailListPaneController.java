package main;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Mail;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MailListPaneController implements Initializable {

    @FXML ListView mailListView;
    @FXML TextField searchFieldTxf;
    @FXML Button searchBtn;
    private String category;
    private String flag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(String category, String flag){
        this.category = category;
        this.flag = flag;
        //TODO get mail cat and flag
        //toString Mail
        Platform.runLater(() -> {
            mailListView.getItems().clear();
            mailListView.getItems().addAll(Main.connectionHandler.mails);
        });
    }

    public void newMailButtonClick(){//TODO
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewMailPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "unread");
    }

    public void viewButtonClick(){
        if (mailListView.getSelectionModel().getSelectedItem() != null) {
            Mail m = (Mail) mailListView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MailDisplayPane.fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            MailDisplayPaneController mdpc = loader.getController();
            mdpc.initData(m, category, flag);
        } else {
            new CustomDialog(Main.stage,"Mail not selected.", "Select mail before viewing.", new JFXButton("Ok")).showDialog();
        }
    }

    public void replyButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "all");
    }

    public void forwardButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewMailPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "unread");
    }

    public void readUnreadButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "all");
    }

    public void deleteButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailListPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailListPaneController mlpc = loader.getController();
        mlpc.initData(category, "all");
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailSecondPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSecondPaneController mspc = loader.getController();
        mspc.initData(category);
    }
}
