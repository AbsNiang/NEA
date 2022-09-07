package com.example.demo.auth.Controllers;

import com.example.demo.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable { //Scene once signed in (customer)
    @FXML
    private TextField tf_keywords;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void setUserInformation(String emailAddress, String firstName) {

    }

}
