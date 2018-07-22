package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import models.Booking;
import models.Transaction;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FinanceAddTransactionPaneController implements Initializable {

    @FXML ComboBox transactionTypeCmb;
    @FXML ComboBox gsNumberCmb;
    @FXML TextField otherTxf;
    @FXML TextField amountTxf;
    @FXML TextField referenceTxf;
    @FXML DatePicker transactionDateDP;
    private Transaction transaction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transactionDateDP.setConverter(new StringConverter<LocalDate>(){
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            @Override
            public String toString(LocalDate localDate){
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }
            @Override
            public LocalDate fromString(String dateString){
                if(dateString==null || dateString.trim().isEmpty()){
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
        otherTxf.setEditable(false);
        transactionTypeCmb.getItems().clear();
        transactionTypeCmb.getItems().addAll("Please Select", "Money Came In", "Supplier Paid", "Expenses");
        transactionTypeCmb.getSelectionModel().select(0);
        gsNumberCmb.getItems().clear();
        gsNumberCmb.getItems().add("Please Select");
        gsNumberCmb.getItems().add("Other");
        for(Booking b: Main.connectionHandler.bookings){
            gsNumberCmb.getItems().add("GS" + b.getGsNumber());
        }
        gsNumberCmb.getSelectionModel().select(0);
        gsNumberCmb.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(gsNumberCmb.getSelectionModel().getSelectedItem().toString().matches("Other")){
                otherTxf.setEditable(true);
            } else {
                otherTxf.setEditable(false);
            }
        });
    }

    public void initData(Transaction transaction){
        this.transaction = transaction;
        transactionTypeCmb.getSelectionModel().select(transaction.getTransactionType());
        gsNumberCmb.getSelectionModel().select(transaction.getGsNumber());
        otherTxf.setText(transaction.getOther());
        referenceTxf.setText(transaction.getReference());
        amountTxf.setText(transaction.getAmount() + "");
        transactionDateDP.setValue(LocalDate.parse(transaction.getTransactionDate()));
    }

    public void addTransactionButtonClick(){
        boolean x = false;
        String other = "NA";
        if(gsNumberCmb.getSelectionModel().getSelectedItem().toString().matches("Other")) {
            if (!otherTxf.getText().matches("")) {
                other = otherTxf.getText();
                x = true;
            } else {
                new CustomDialog(Main.stage,"Other not entered", "Enter other before adding transaction.", new JFXButton("Ok")).showDialog();
                x = false;
            }
        } else {
            other = "NA";
            x = true;
        }
        if(x) {
            if (!transactionTypeCmb.getSelectionModel().getSelectedItem().toString().matches("Please Select")) {
                if (!gsNumberCmb.getSelectionModel().getSelectedItem().toString().matches("Please Select")) {
                    if (!referenceTxf.getText().matches("")) {
                        if (!amountTxf.getText().matches("")) {
                            if (!transactionDateDP.getValue().toString().matches("")) {
                                if (transaction == null) {
                                    transaction = new Transaction(-1, transactionTypeCmb.getSelectionModel().getSelectedItem().toString(), gsNumberCmb.getSelectionModel().getSelectedItem().toString(), other, referenceTxf.getText(), Double.parseDouble(amountTxf.getText()), transactionDateDP.getValue().toString());
                                } else {
                                    transaction = new Transaction(transaction.getID(), transactionTypeCmb.getSelectionModel().getSelectedItem().toString(), gsNumberCmb.getSelectionModel().getSelectedItem().toString(), other, referenceTxf.getText(), Double.parseDouble(amountTxf.getText()), transactionDateDP.getValue().toString());
                                }
                                Main.connectionHandler.outputQueue.add(transaction);
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("FinancePane.fxml"));
                                try {
                                    Main.setStage(loader.load());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                new CustomDialog(Main.stage, "Transaction date not selected", "Select transaction date before adding transaction.", new JFXButton("Ok")).showDialog();
                            }
                        } else {
                            new CustomDialog(Main.stage, "Amount not entered", "Enter amount before adding transaction.", new JFXButton("Ok")).showDialog();
                        }
                    } else {
                        new CustomDialog(Main.stage, "Reference not entered", "Enter reference before adding transaction.", new JFXButton("Ok")).showDialog();
                    }
                } else {
                    new CustomDialog(Main.stage, "GSNumber not selected", "Select GSNumber before adding transaction.", new JFXButton("Ok")).showDialog();
                }
            } else {
                new CustomDialog(Main.stage, "Transaction type not selected", "Select transaction type before adding transaction.", new JFXButton("Ok")).showDialog();
            }
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FinancePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
