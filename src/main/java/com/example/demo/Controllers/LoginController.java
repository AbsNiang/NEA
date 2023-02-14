package com.example.demo.Controllers;

import com.example.demo.Database.UserTable;
import com.example.demo.EmailHandling.Email;
import com.example.demo.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

        btn_login.setOnAction(event -> {
            if (!tf_username.getText().equals("") || !pf_password.getText().equals("")) {
                if (Email.checkEmail(tf_username.getText())) {
                    UserTable.logInUser(event, tf_username.getText(), pf_password.getText());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Email isn't in correct format.");
                    alert.show();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all fields.");
                alert.show();
            }
        });

        btn_signup.setOnAction(event -> SceneHandler.changeScene(event, "SignUp.fxml", "Sign-Up", tf_username.getText(), 600, 400));

        btn_forgot.setOnAction(actionEvent -> SceneHandler.changeScene(actionEvent, "ForgotPassword.fxml", "Forgot Password", tf_username.getText(), 600, 400));
    }

}