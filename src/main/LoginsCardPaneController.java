package main;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import models.Login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginsCardPaneController implements Initializable {

    @FXML Label loginName;
    @FXML Label username;
    @FXML Label password;
    @FXML Button editBtn;
    @FXML Button removeBtn;
    private Login login;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Login login){
        Main.connectionHandler.logins.addListener((InvalidationListener) e -> {
            loginName.setText(login.getLoginName());
            username.setText(login.getUsername());
            password.setText(login.getPassword());
            this.login = login;
        });
        loginName.setText(login.getLoginName());
        username.setText(login.getUsername());
        password.setText(login.getPassword());
        this.login = login;
        editBtn.setTooltip(new Tooltip("Edit"));
        removeBtn.setTooltip(new Tooltip("Remove"));
    }

    public void editButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewLoginPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewLoginPaneController nlpc = loader.getController();
        nlpc.initData(login);
    }

    public void removeButtonClick(){
        if (UserNotification.confirmationDialog(Main.stage, "Are you sure you want to remove " + login.getLoginName() + "?", "This will no longer be saved on the system.")) {
            Main.connectionHandler.outputQueue.add("rl:" + login.getLoginID());
        }
    }
}
