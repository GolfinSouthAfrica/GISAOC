package main;

import com.jfoenix.controls.JFXButton;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FinancePaneController implements Initializable {

    @FXML TabPane tabs;
    @FXML TextField searchTxf;
    @FXML TableView moneyCameInTV;
    @FXML TableColumn mciFirst;
    @FXML TableColumn mciSecond;
    @FXML TableColumn mciThird;
    @FXML TableColumn mciFouth;
    @FXML TableColumn mciFifth;
    @FXML TableView moneyPaidOutTV;
    @FXML TableColumn mpoFirst;
    @FXML TableColumn mpoSecond;
    @FXML TableColumn mpoThird;
    @FXML TableColumn mpoFouth;
    @FXML TableColumn mpoFifth;
    @FXML TableColumn mpoSixth;
    @FXML TableView expensesTV;
    @FXML TableColumn exFirst;
    @FXML TableColumn exSecond;
    @FXML TableColumn exThird;
    @FXML TableColumn exFouth;
    @FXML TableColumn exFifth;
    @FXML TableView suppliersPaidTV;
    @FXML TableColumn spFirst;
    @FXML TableColumn spSecond;
    @FXML TableColumn spThird;
    @FXML TableColumn spFouth;
    @FXML TableColumn spFifth;
    @FXML TableView searchTV;
    @FXML TableColumn seFirst;
    @FXML TableColumn seSecond;
    @FXML TableColumn seThird;
    @FXML TableColumn seFouth;
    @FXML TableColumn seFifth;
    @FXML TableColumn seSixth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mciFirst.setCellValueFactory(new PropertyValueFactory<Transaction, String>("gsNumber"));
        mciSecond.setCellValueFactory(new PropertyValueFactory<Transaction, String>("other"));
        mciThird.setCellValueFactory(new PropertyValueFactory<Transaction, String>("reference"));
        mciFouth.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionDate"));
        mciFifth.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));

        mpoFirst.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionType"));
        mpoSecond.setCellValueFactory(new PropertyValueFactory<Transaction, String>("gsNumber"));
        mpoThird.setCellValueFactory(new PropertyValueFactory<Transaction, String>("other"));
        mpoFouth.setCellValueFactory(new PropertyValueFactory<Transaction, String>("reference"));
        mpoFifth.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionDate"));
        mpoSixth.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));

        exFirst.setCellValueFactory(new PropertyValueFactory<Transaction, String>("gsNumber"));
        exSecond.setCellValueFactory(new PropertyValueFactory<Transaction, String>("other"));
        exThird.setCellValueFactory(new PropertyValueFactory<Transaction, String>("reference"));
        exFouth.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionDate"));
        exFifth.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));

        spFirst.setCellValueFactory(new PropertyValueFactory<Transaction, String>("gsNumber"));
        spSecond.setCellValueFactory(new PropertyValueFactory<Transaction, String>("other"));
        spThird.setCellValueFactory(new PropertyValueFactory<Transaction, String>("reference"));
        spFouth.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionDate"));
        spFifth.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));

        seFirst.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionType"));
        seSecond.setCellValueFactory(new PropertyValueFactory<Transaction, String>("other"));
        seThird.setCellValueFactory(new PropertyValueFactory<Transaction, String>("gsNumber"));
        seFouth.setCellValueFactory(new PropertyValueFactory<Transaction, String>("reference"));
        seFifth.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionDate"));
        seSixth.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));

        Main.connectionHandler.transactions.addListener((InvalidationListener) e -> {
            populateTables();
        });
        populateTables();
    }

    private void populateTables(){
        ObservableList<Transaction>mci = FXCollections.observableArrayList();
        ObservableList<Transaction>mpo = FXCollections.observableArrayList();
        ObservableList<Transaction>ex = FXCollections.observableArrayList();
        ObservableList<Transaction>sp = FXCollections.observableArrayList();
        for(Transaction t:Main.connectionHandler.transactions){
            if(t.getTransactionType().matches("Money Came In")){
                mci.add(t);
            } else if (t.getTransactionType().matches("Supplier Paid")) {
                sp.add(t);
                mpo.add(t);
            } else if (t.getTransactionType().matches("Expenses")) {
                ex.add(t);
                mpo.add(t);
            }
        }
        moneyCameInTV.setItems(mci);
        moneyPaidOutTV.setItems(mpo);
        expensesTV.setItems(ex);
        suppliersPaidTV.setItems(sp);
    }

    public void searchButtonClick(){
        ObservableList<Transaction>se = FXCollections.observableArrayList();
        if (!searchTxf.getText().matches("")) {
            for (Transaction t: Main.connectionHandler.transactions) {
                if (t.getTransactionType().toLowerCase().contains(searchTxf.getText().toLowerCase()) || t.getGsNumber().toLowerCase().contains(searchTxf.getText().toLowerCase()) || t.getOther().toLowerCase().contains(searchTxf.getText().toLowerCase()) || t.getTransactionDate().toLowerCase().contains(searchTxf.getText().toLowerCase())) {
                    se.add(t);
                }
            }
        } else {
            searchTV.getItems().clear();
        }
        searchTV.setItems(se);
    }

    public void addTransactionButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FinanceAddTransaction.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editTransactionButtonClick(){
        if(tabs.getSelectionModel().getSelectedItem().getText().matches("Money Came In") && moneyCameInTV.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FinanceAddTransaction.fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            FinanceAddTransactionPaneController fatpc = loader.getController();
            fatpc.initData((Transaction) moneyCameInTV.getSelectionModel().getSelectedItem());
        } else if (tabs.getSelectionModel().getSelectedItem().getText().matches("Money Paid Out") && moneyPaidOutTV.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FinanceAddTransaction.fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            FinanceAddTransactionPaneController fatpc = loader.getController();
            fatpc.initData((Transaction) moneyPaidOutTV.getSelectionModel().getSelectedItem());
        } else if (tabs.getSelectionModel().getSelectedItem().getText().matches("Expenses") && expensesTV.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FinanceAddTransaction.fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            FinanceAddTransactionPaneController fatpc = loader.getController();
            fatpc.initData((Transaction) expensesTV.getSelectionModel().getSelectedItem());
        } else if (tabs.getSelectionModel().getSelectedItem().getText().matches("Suppliers Paid") && suppliersPaidTV.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FinanceAddTransaction.fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            FinanceAddTransactionPaneController fatpc = loader.getController();
            fatpc.initData((Transaction) suppliersPaidTV.getSelectionModel().getSelectedItem());
        } else if (tabs.getSelectionModel().getSelectedItem().getText().matches("Search") && searchTV.getSelectionModel().getSelectedItem() != null){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FinanceAddTransaction.fxml"));
            try {
                Main.setStage(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            FinanceAddTransactionPaneController fatpc = loader.getController();
            fatpc.initData((Transaction) searchTV.getSelectionModel().getSelectedItem());
        } else {
            new CustomDialog(Main.stage, "No Transaction Selected", "Select the transaction you want to edit first", new JFXButton("Ok")).showDialog();
        }
    }

    public void removeTransactionButtonClick(){
        if(tabs.getSelectionModel().getSelectedItem().getText().matches("Money Came In") && moneyCameInTV.getSelectionModel().getSelectedItem() != null){
            Main.connectionHandler.outputQueue.add("rtr:" + ((Transaction)moneyCameInTV.getSelectionModel().getSelectedItem()).getID());
        } else if (tabs.getSelectionModel().getSelectedItem().getText().matches("Money Paid Out") && moneyPaidOutTV.getSelectionModel().getSelectedItem() != null){
            Main.connectionHandler.outputQueue.add("rtr:" + ((Transaction)moneyPaidOutTV.getSelectionModel().getSelectedItem()).getID());
        } else if (tabs.getSelectionModel().getSelectedItem().getText().matches("Expenses") && expensesTV.getSelectionModel().getSelectedItem() != null){
            Main.connectionHandler.outputQueue.add("rtr:" + ((Transaction)expensesTV.getSelectionModel().getSelectedItem()).getID());
        } else if (tabs.getSelectionModel().getSelectedItem().getText().matches("Suppliers Paid") && suppliersPaidTV.getSelectionModel().getSelectedItem() != null){
            Main.connectionHandler.outputQueue.add("rtr:" + ((Transaction)suppliersPaidTV.getSelectionModel().getSelectedItem()).getID());
        } else if (tabs.getSelectionModel().getSelectedItem().getText().matches("Search") && searchTV.getSelectionModel().getSelectedItem() != null){
            Main.connectionHandler.outputQueue.add("rtr:" + ((Transaction)searchTV.getSelectionModel().getSelectedItem()).getID());
        } else {
            new CustomDialog(Main.stage, "No Transaction Selected", "Select the transaction you want to remove first", new JFXButton("Ok")).showDialog();
        }
    }

    public void backButtonClick(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePane.fxml"));
        try {
            Main.setStage(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
