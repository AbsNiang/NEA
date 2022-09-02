package com.example.demo.auth;

import com.example.demo.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable { //Sign up scene
    @FXML
    private Button btn_signup;
    @FXML
    private Button btn_alreadyhaveaccount;
    @FXML
    private TextField tf_firstname;
    @FXML
    private TextField tf_surname;
    @FXML
    private TextField tf_username;
    @FXML
    PasswordField pf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_username.getText().isEmpty() && !pf_password.getText().trim().isEmpty()
                        && !tf_firstname.getText().trim().isEmpty() && !tf_surname.getText().trim().isEmpty()) {
                    DBUtils.signUpUser(event, tf_username.getText(), pf_password.getText(), tf_firstname.getText(), tf_surname.getText(), false);
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all the information");
                    alert.show();
                }
            }
        });
       btn_alreadyhaveaccount.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               DBUtils.changeScene(event, "Login.fxml","Login",null);
           }
       });
    }
}
