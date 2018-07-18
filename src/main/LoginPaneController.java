package main;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPaneController implements Initializable {

    FXMLLoader loader;

    @FXML private TextField emailTxf;
    @FXML private TextField passwordTxf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void start(){
        Platform.runLater(()-> emailTxf.requestFocus());
    }

    public void loginButtonClick() {
        if (emailTxf.getText().length() < 8) {
            emailTxf.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(20), BorderWidths.DEFAULT)));
            Tooltip studentNumberShortTooltip = new Tooltip("Username too short");
            studentNumberShortTooltip.getStyleClass().add("login-tooltip");
            emailTxf.setTooltip(studentNumberShortTooltip);
            Platform.runLater(()-> emailTxf.requestFocus());
        } else if (passwordTxf.getText().length() < 8) {
            passwordTxf.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(20), BorderWidths.DEFAULT)));
            Tooltip passwordShortTooltip = new Tooltip("Password too short");
            passwordShortTooltip.getStyleClass().add("login-tooltip");
            passwordTxf.setTooltip(passwordShortTooltip);
            Platform.runLater(()-> passwordTxf.requestFocus());
        } else {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LoadingPane.fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            BooleanProperty waitingForAuthorisation = new SimpleBooleanProperty(true);
            BooleanProperty authoriseResult = new SimpleBooleanProperty(false);
            Thread loginThread = new Thread(() -> {
                if (Main.connectionHandler.authorise(emailTxf.getText(), passwordTxf.getText())) {
                    while (!Main.connectionHandler.userInitialized()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    authoriseResult.setValue(true);
                } else {
                    authoriseResult.setValue(false);
                }
                waitingForAuthorisation.set(false);
            });
            loginThread.start();
            waitingForAuthorisation.addListener(al -> {
                if (authoriseResult.getValue()) {
                    Platform.runLater(() -> {
                        loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("HomePane.fxml"));
                        try {
                            Main.setStage(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("LoginPane.fxml"));
                        try {
                            Main.setStage(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        passwordTxf.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(20), BorderWidths.DEFAULT)));
                        Tooltip incorrectLoginTooltip = new Tooltip("Incorrect login details");
                        incorrectLoginTooltip.setStyle("-fx-background-color: #28bbff;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-radius: 10;" +
                                "-fx-border-color: white;" +
                                "-fx-text-fill: white;");
                        passwordTxf.setTooltip(incorrectLoginTooltip);
                        passwordTxf.clear();
                        passwordTxf.requestFocus();
                    });
                }
            });
        }
    }

    public void forgotPasswordHLClicked(){
        new ForgotPasswordDialog(Main.stage, Main.connectionHandler).showDialog();
    }
}
