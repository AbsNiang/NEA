package com.example.demo.Controllers;

import com.example.demo.Database.UserTable;
import com.example.demo.EmailHandling.Email;
import com.example.demo.EmailHandling.EmailToken;
import com.example.demo.General.Repository;
import com.example.demo.Objects.User;
import com.example.demo.Registration.GenerateConfirmationCode;
import com.example.demo.Registration.PasswordConverter;
import com.example.demo.Registration.PasswordHandler;
import com.example.demo.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    @FXML
    private AnchorPane signUp_form;
    @FXML
    private AnchorPane enterCode_form;

    private String code;
    private String email;
    private String password;
    private String firstName;
    private String surname;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_signup.setOnAction(event -> {
            if (tf_code.getText().equals(code)) {
                System.out.println("code is correct");
                byte[] salt = PasswordHandler.generateSalt();
                byte[] byteDigestPassword = PasswordHandler.getSaltedHash(password, salt);
                String hashedPassword = PasswordConverter.toHex(byteDigestPassword);
                String strSalt = PasswordConverter.toHex(salt);
                User user = new User(email, hashedPassword, strSalt, firstName, surname, false);
                UserTable.signUpUser(event, user);
            } else {
                Repository.giveAlert("Code entered is incorrect.", "error");
            }
        });

        //redirects to login page
        btn_alreadyhaveaccount.setOnAction(event -> SceneHandler.changeScene(event, "Login.fxml", "Log-in", null, 600, 400));

        //send confirmation email
        btn_confirm.setOnAction(actionEvent -> {
            code = GenerateConfirmationCode.generateCode(); //gets a random 6 digit code
            setDetails();
            EmailToken emailToken = new EmailToken(email, code, "Use the code below to confirm your email and sign up:", "This is a confirmation email.", true);
            Email.sendEmail(emailToken);
            switchForm();
        });
    }

    private void switchForm() {
        signUp_form.setVisible(false);
        enterCode_form.setVisible(true);

    }

    private void setDetails() {
        if (!tf_username.getText().isEmpty() && !pf_password.getText().trim().isEmpty()
                && !tf_firstname.getText().trim().isEmpty() && !tf_surname.getText().trim().isEmpty() && PasswordHandler.passwordCheck(pf_password.getText())) {
            if (Email.checkEmail(tf_username.getText())) {
                email = tf_username.getText();
                password = pf_password.getText();
                firstName = tf_firstname.getText();
                surname = tf_surname.getText();
            } else {
                Repository.giveAlert("Email isn't in the correct format.", "error");
            }
        } else {
            Repository.giveAlert("Please fill in all the information. " +
                    "\nPasswords must contain at least 8 characters including upper and lowercase, 1 number, and 1 special character.", "error");

        }
    }
}
