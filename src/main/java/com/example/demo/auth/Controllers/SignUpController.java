package com.example.demo.auth.Controllers;

import com.example.demo.DBUtils.UserTable;
import com.example.demo.SceneHandler;
import com.example.demo.auth.EmailHandling.EmailToken;
import com.example.demo.auth.Objects.User;
import com.example.demo.auth.EmailHandling.Email;
import com.example.demo.auth.Registration.GenerateConfirmationCode;
import com.example.demo.auth.Registration.PasswordConverter;
import com.example.demo.auth.Registration.PasswordHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable { //Add toggle password visibility for QOL improvements, dynamic panes
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
    private PasswordField pf_password;
    @FXML
    private TextField tf_code;
    @FXML
    private Button btn_confirm;

    private String code;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_username.getText().isEmpty() && !pf_password.getText().trim().isEmpty()
                        && !tf_firstname.getText().trim().isEmpty() && !tf_surname.getText().trim().isEmpty() && PasswordHandler.passwordCheck(pf_password.getText())) {
                    if (tf_code.getText().equals(code)) {
                        System.out.println("code is correct");
                        byte[] salt = PasswordHandler.generateSalt();
                        byte[] byteDigestPassword = PasswordHandler.getSaltedHash(pf_password.getText(), salt);
                        String hashedPassword = PasswordConverter.toHex(byteDigestPassword);
                        String strSalt = PasswordConverter.toHex(salt);
                        User user = new User(tf_username.getText(), hashedPassword, strSalt, tf_firstname.getText(), tf_surname.getText(), false, false);
                        UserTable.signUpUser(event, user);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert.setContentText("Code entered is incorrect.");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.setContentText("Please fill in all the information. " +
                            "\nPasswords must contain at least 8 characters including upper and lowercase, 1 number, and 1 special character.");
                    alert.show();
                }
            }
        });

        btn_alreadyhaveaccount.setOnAction(new EventHandler<ActionEvent>() {//redirects to login page
            @Override
            public void handle(ActionEvent event) {
                SceneHandler.changeScene(event, "Login.fxml", "Log-in", null, 600, 400);
            }
        });

        btn_confirm.setOnAction(new EventHandler<ActionEvent>() { //send confirmation email
            @Override
            public void handle(ActionEvent actionEvent) {
                code = GenerateConfirmationCode.generateCode(); //gets a random 6 digit code
                EmailToken emailToken = new EmailToken(tf_username.getText(), code, "Use the code below to confirm your email and sign up:", "This is a confirmation email.", true);
                Email.sendEmail(emailToken);
            }
        });
    }
}
