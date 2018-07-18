package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import models.Booking;
import models.DataFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuotePreviewPaneController implements Initializable {

    Booking booking;
    String textMessage;
    boolean newBooking;
    int fileLength;
    String lastPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Booking booking, String textMessage, boolean newBooking, int fileLength, String lastPane){
        this.lastPane = lastPane;
        this.booking = booking;
        this.textMessage = textMessage;
        this.newBooking = newBooking;
        this.fileLength = fileLength;
    }

    public void viewQuoteButtonClick(){
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
    }

    public void sendAndSaveButtonClick(){
        Main.connectionHandler.outputQueue.add("snd:Costing:" + booking.getGsNumber() + ":" + booking.getEmail() + ":" + booking.getClientName() + ":" + textMessage);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(lastPane + ".fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWithoutSendingButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(lastPane + ".fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewQuotePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        NewQuotePaneController nqp = loader.getController();
        nqp.initEditData(lastPane, booking, true);
    }
}
