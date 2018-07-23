package main;

import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.ContactDetails;
import models.Supplier;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class NewSupplierContactPaneController implements Initializable {

    @FXML TextField contactNameLblTxf;
    @FXML TextField positionTxf;
    @FXML TextField numberTxf;
    @FXML TextField emailTxf;
    private ContactDetails cd;
    private Supplier supplier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Supplier supplier){
        Main.connectionHandler.suppliers.addListener((InvalidationListener) e -> {
            this.supplier = supplier;
        });
        this.supplier = supplier;
    }

    public void initEditData(ContactDetails cd, Supplier supplier){
        Main.connectionHandler.suppliers.addListener((InvalidationListener) e -> {
            this.supplier = supplier;
            this.cd = cd;
            contactNameLblTxf.setText(cd.getPersonName());
            positionTxf.setText(cd.getPosition());
            numberTxf.setText(cd.getNumber());
            emailTxf.setText(cd.getEmail());
        });
        this.cd = cd;
        this.supplier = supplier;
        contactNameLblTxf.setText(cd.getPersonName());
        positionTxf.setText(cd.getPosition());
        numberTxf.setText(cd.getNumber());
        emailTxf.setText(cd.getEmail());
    }

    public void addButtonClick(){
        if (!contactNameLblTxf.getText().matches("")) {
            if (!positionTxf.getText().matches("")) {
                if (!numberTxf.getText().matches("")) {
                    if (!emailTxf.getText().matches("")) {
                        if (cd == null) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate localDate = LocalDate.now();
                            Main.connectionHandler.outputQueue.add(new ContactDetails(supplier.getSupplierNumber() + 100000, contactNameLblTxf.getText(), positionTxf.getText(), numberTxf.getText(), emailTxf.getText(), dtf.format(localDate)));
                        } else {
                            Main.connectionHandler.outputQueue.add(new ContactDetails(cd.getContactDetailsID(), contactNameLblTxf.getText(), positionTxf.getText(), numberTxf.getText(), emailTxf.getText(), cd.getDateAdded()));
                        }
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("SuppliersPane.fxml"));
                        try {
                            Main.setStage(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new CustomDialog().CustomDialog(Main.stage,"Email not entered", "Enter Email before adding Contact.", new JFXButton("Ok"));
                    }
                } else {
                    new CustomDialog().CustomDialog(Main.stage,"Number not selected", "Select Number before adding Contact.", new JFXButton("Ok"));
                }
            } else {
                new CustomDialog().CustomDialog(Main.stage,"Position not selected", "Select Position before adding Contact.", new JFXButton("Ok"));
            }
        } else {
            new CustomDialog().CustomDialog(Main.stage,"Contact Name not entered", "Enter Contact Name before adding Contact.", new JFXButton("Ok"));
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewSuppliersPane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewSuppliersPaneController vspc = loader.getController();
        vspc.initData(supplier);
    }

}
