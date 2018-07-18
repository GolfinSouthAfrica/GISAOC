package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FinancePaneController implements Initializable {

    @FXML TextField searchTxf;
    @FXML TableView moneyCameInTV;
    @FXML TableView moneyPaidOutTV;
    @FXML TableView expensesTV;
    @FXML TableView suppliersPaidTV;
    @FXML TableView searchTV;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
