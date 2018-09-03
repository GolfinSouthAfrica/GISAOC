package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Login;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Login");
        alert.setHeaderText("Remove Login");
        alert.setContentText("Are you sure you want to remove the login (" + login.getLoginName() + ")?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Main.connectionHandler.outputQueue.add("rl:" + login.getLoginID());
        }
    }
}
