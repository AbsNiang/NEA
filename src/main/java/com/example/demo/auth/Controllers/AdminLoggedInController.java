package com.example.demo.auth.Controllers;

import com.example.demo.SceneHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLoggedInController implements Initializable { //Scene once signed in (customer)
    @FXML
    private Button btn_signout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_signout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneHandler.changeScene(actionEvent, "Login.fxml", "Login", null, 600, 400);
            }
        });

    }

    public void setUserInformation(String emailAddress, String firstName) {

    }

}
