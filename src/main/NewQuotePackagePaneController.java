package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class NewQuotePackagePaneController implements Initializable{

    @FXML ListView availableListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(){

    }

    public void packageSelectButtonClick(){

    }

    public Object getPackage(){//TODO
        if(availableListView.getSelectionModel().getSelectedItem() != null){
            return availableListView.getSelectionModel().getSelectedItem();
        } else {
            return null;
        }

    }

}

