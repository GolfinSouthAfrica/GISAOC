package main;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Mail;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MailSecondPaneController implements Initializable {

    private String category;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(String category){
        this.category = category;
    }

    public void initializeMails(String category, String flag){
        final FXMLLoader[] loader = {new FXMLLoader()};
        loader[0].setLocation(getClass().getResource("LoadingPane.fxml"));
        try {
            Main.setStage(loader[0].load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.connectionHandler.getMails(category, flag);
        Main.connectionHandler.gotMails.addListener(al -> {
            if(Main.connectionHandler.gotMails.getValue()) {
                Platform.runLater(() -> {
                    loader[0] = new FXMLLoader();
                    loader[0].setLocation(getClass().getResource("MailListPane.fxml"));
                    try {
                        Main.setStage(loader[0].load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MailListPaneController mlpc = new MailListPaneController();
                    mlpc.initData(category, flag);
                });
            }
        });
    }

    public void unreadButtonClick(){
        initializeMails(category, "unread");
    }

    public void allButtonClick(){
        initializeMails(category, "all");
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MailPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
