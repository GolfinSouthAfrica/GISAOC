package main;

import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;
import models.Booking;
import models.DataFile;

import java.io.File;

public class QuotePreviewPane extends CustomDialogSkin {

    Booking booking;
    String textMessage;
    int fileLength;


    public QuotePreviewPane(Window parent, Booking booking, String textMessage, int fileLength) {
        this.booking = booking;
        this.textMessage = textMessage;
        this.fileLength = fileLength;
        initOwner(parent);

        Button viewQuote = new Button();
        viewQuote.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("../resources/Add.png"))));
        viewQuote.getStyleClass().add("button");
        viewQuote.setOnAction(event -> {
            DataFile costing = new DataFile("Quote", "GS" + booking.getGsNumber(), ".xls", fileLength);
            ConnectionHandler.FileDownloader fileDownloader = Main.connectionHandler.new FileDownloader(costing);
            fileDownloader.start();
            costing.setFileDownloader(fileDownloader);
            Main.connectionHandler.user.update();
            fileDownloader.done.addListener((InvalidationListener) ea -> {
                File openFile = new File(Main.LOCAL_CACHE.getAbsolutePath() + "/" + costing.getFileType() + "/" + costing.getFileName() + ".xls");
                if (openFile.exists() && openFile.length() == costing.getFileLength()) {
                    try {
                        java.awt.Desktop.getDesktop().open(openFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });
        Text viewQuoteLbl = new Text("View Quote Again");
        viewQuoteLbl.getStyleClass().add("home-text");
        VBox button1 = new VBox(viewQuote, viewQuoteLbl);
        button1.setAlignment(Pos.CENTER);

        Button sendSave = new Button();
        sendSave.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("../resources/Add.png"))));
        sendSave.getStyleClass().add("button");
        sendSave.setOnAction(event -> {
            Main.connectionHandler.outputQueue.add("snd:Costing:" + booking.getGsNumber() + ":" + booking.getEmail() + ":" + booking.getClientName() + ":" + textMessage);
            Main.quoteDone.setValue(true);
            closeAnimation();
        });
        Text sendSaveLbl = new Text("Send & Save");
        sendSaveLbl.getStyleClass().add("home-text");
        VBox button2 = new VBox(sendSave, sendSaveLbl);
        button2.setAlignment(Pos.CENTER);

        Button sendNotSave = new Button();
        sendNotSave.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("../resources/Add.png"))));
        sendNotSave.getStyleClass().add("button");
        sendNotSave.setOnAction(event -> {
            Main.quoteDone.setValue(true);
            closeAnimation();
        });
        Text sendNotSaveLbl = new Text("Send Without Saving");
        sendNotSaveLbl.getStyleClass().add("home-text");
        VBox button3 = new VBox(sendNotSave, sendNotSaveLbl);
        button3.setAlignment(Pos.CENTER);

        Button back = new Button();
        back.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("../resources/Back.png"))));
        back.getStyleClass().add("button");
        back.setOnAction(event -> {
            closeAnimation();
        });
        Text backLbl = new Text("Back");
        backLbl.getStyleClass().add("home-text");
        VBox button4 = new VBox(back, backLbl);
        button4.setAlignment(Pos.CENTER);

        HBox top = new HBox(button1, button2, button3);
        top.setAlignment(Pos.CENTER);
        top.setPrefWidth(600);
        top.setPrefHeight(200);
        top.setSpacing(20);
        HBox bottom = new HBox(button4);
        bottom.setPrefWidth(600);
        bottom.setPrefHeight(200);
        bottom.setAlignment(Pos.CENTER);
        VBox settingsInnerPane = new VBox(top, bottom);
        settingsInnerPane.getChildren().addAll();
        settingsInnerPane.setSpacing(15);
        settingsInnerPane.setPadding(new Insets(20));
        settingsInnerPane.setAlignment(Pos.TOP_CENTER);
        settingsInnerPane.setStyle("-fx-background-color: #595959;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 2;" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;");
        settingsInnerPane.setMaxSize(600, 400);
        settingsInnerPane.setMinSize(600, 400);
        VBox settingsPane = new VBox(settingsInnerPane);
        setWidth(600);
        settingsPane.setAlignment(Pos.CENTER);
        getDialogPane().setContent(settingsPane);
        showDialog();
    }

}
