package main;

import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.Login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewLoginPaneController implements Initializable {

    @FXML TextField loginNameTxf;
    @FXML TextField usernameTxf;
    @FXML TextField passwordTxf;
    private Login login;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Login login){
        Main.connectionHandler.logins.addListener((InvalidationListener) e -> {
            loginNameTxf.setText(login.getLoginName());
            usernameTxf.setText(login.getUsername());
            passwordTxf.setText(login.getPassword());
            this.login = login;
        });
        loginNameTxf.setText(login.getLoginName());
        usernameTxf.setText(login.getUsername());
        passwordTxf.setText(login.getPassword());
        this.login = login;
    }

    public void addButtonClick(){
        if (!loginNameTxf.getText().matches("")) {
            if (!usernameTxf.getText().matches("")) {
                if (!passwordTxf.getText().matches("")) {
                    if (login == null) {
                        Main.connectionHandler.outputQueue.add(new Login(-1, loginNameTxf.getText(), usernameTxf.getText(), passwordTxf.getText()));
                    } else {
                        Main.connectionHandler.outputQueue.add(new Login(login.getLoginID(), loginNameTxf.getText(), usernameTxf.getText(), passwordTxf.getText()));
                    }
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("LoginsPane.fxml"));
                    try {
                        Main.setStage(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    new CustomDialog().CustomDialog(Main.stage,"Password not entered", "Enter Password before adding Login.", new JFXButton("Ok"));
                }
            } else {
                new CustomDialog().CustomDialog(Main.stage,"Username not entered", "Enter Username before adding Login.", new JFXButton("Ok"));
            }
        } else {
            new CustomDialog().CustomDialog(Main.stage,"Login Name not entered", "Enter Login Name before adding Login.", new JFXButton("Ok"));
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginsPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
