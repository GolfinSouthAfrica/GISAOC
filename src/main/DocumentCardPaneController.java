package main;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import models.DataFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DocumentCardPaneController {

    @FXML Label documentNameLbl;
    @FXML Button viewBtn;
    @FXML Button removeBtn;
    @FXML Button sendBtn;
    @FXML Button exportBtn;
    @FXML VBox documentNameVBox;
    @FXML HBox buttonHBox;
    private DataFile dataFile;

    public void initData(DataFile dataFile){
        this.dataFile = dataFile;
        documentNameLbl.setText(dataFile.getFileName());
        viewBtn.setTooltip(new Tooltip("View"));
        removeBtn.setTooltip(new Tooltip("Remove"));
        sendBtn.setTooltip(new Tooltip("Email"));
        exportBtn.setTooltip(new Tooltip("Export"));
    }

    public void openButtonClick(){
        File openFile;
        if ((openFile = new File("G:/My Drive/d. Documents/" + dataFile.getFileName() + dataFile.getFileExtension())).exists()) {
            try {
                java.awt.Desktop.getDesktop().open(openFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void removeButtonClick(){
        boolean rc = UserNotification.confirmationDialog(Main.stage, "Delete Document", "Are you sure you want to delete Document: " + dataFile.getFileName() + "?");
        if (rc) {
            Main.connectionHandler.outputQueue.add("rd:" + dataFile.getFileName() + dataFile.getFileExtension());
        }
    }

    public void sendButtonClick(){//TODO test
        new EmailDialog(Main.stage, dataFile.getFileType(), dataFile.getFileName()).showDialog();
    }

    public void exportButtonClick(){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose Directory to export to..");
        File f = dc.showDialog(Main.stage);
        if (f != null) {
            File source = new File("G:/My Drive/d. Documents/" + dataFile.getFileName() + dataFile.getFileExtension());
            File target = new File(f.getAbsolutePath() + "/" + dataFile.getFileName() + dataFile.getFileExtension());
            target.mkdirs();
            try {
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                new CustomDialog(Main.stage, "Export Successful", "You successfully exported your " + dataFile.getFileType()).showDialog();
            } catch (IOException ex) {
                ex.printStackTrace();
                new CustomDialog(Main.stage, "Export Failed", "The export of your " + dataFile.getFileType() + " failed.").showDialog();
            }
        }
    }

}
