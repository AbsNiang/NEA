package com.example.demo.auth.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable { //Scene once signed in (customer)
    @FXML
    private TextField tf_keywords;
    @FXML
    private Button btn_test;
    private String email;
    private String name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_test.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(email + name);
            }
        });

    }

    public void setUserInformation(String emailAddress, String firstName) {
        email = emailAddress;
        name = firstName;
    }

}
