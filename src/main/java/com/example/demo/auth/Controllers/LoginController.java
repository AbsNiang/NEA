package com.example.demo.auth.Controllers;

import com.example.demo.Database.UserTable;
import com.example.demo.SceneHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_forgot; //forgot password button
    @FXML
    private Button btn_signup; // don't have an account button
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField pf_password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserTable.logInUser(event, tf_username.getText(), pf_password.getText());
            }
        });

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneHandler.changeScene(event, "SignUp.fxml", "Sign-Up", null, 600, 400);
            }
        });

        btn_forgot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneHandler.changeScene(actionEvent,"ForgotPassword.fxml","Forgot Password",null,600,400);
            }
        });
    }

}