package com.example.demo.Controllers;

import com.example.demo.Database.UserTable;
import com.example.demo.EmailHandling.Email;
import com.example.demo.EmailHandling.EmailToken;
import com.example.demo.General.Repository;
import com.example.demo.Registration.GenerateConfirmationCode;
import com.example.demo.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {
    @FXML
    private Button btn_login;

    @FXML
    private AnchorPane forgotPassword_form;
    @FXML
    private Button btn_send;
    @FXML
    private TextField tf_email;

    @FXML
    private AnchorPane enterCode_form;
    @FXML
    private Button btn_submit;
    @FXML
    private TextField tf_entercode;
    @FXML
    private Button btn_resend;
    private String code;
    private String email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_send.setOnAction(actionEvent -> {
            email = tf_email.getText();
            if (UserTable.checkUserExists(email)) {
                sendCode();
                switchForm();
            } else {

                Repository.giveAlert("This user doesn't exist.", "error");

            }
        });

        btn_submit.setOnAction(actionEvent -> {
            if (tf_entercode.getText().equals(code)) {
                SceneHandler.changeScene(actionEvent, "ResetPassword.fxml", "Reset Password",
                        email, 600, 400);

            } else {
                Repository.giveAlert("The entered code is incorrect. " +
                        "\nPress the send code button again or re enter the code correctly.", "error");

            }
        });

        btn_login.setOnAction(actionEvent -> SceneHandler.changeScene(actionEvent, "Login.fxml", "Login", null, 600, 400));

        btn_resend.setOnAction(event -> sendCode());
    }

    private void switchForm() {
        forgotPassword_form.setVisible(false);
        enterCode_form.setVisible(true);
    }

    private void sendCode() {
        code = GenerateConfirmationCode.generateCode(); //gets a random 6 digit code
        EmailToken emailToken = new EmailToken(email,
                code,
                "This email has been sent to reset your password.",
                "The code below should be entered to redirect you to the reset password page.",
                true);
        Email.sendEmail(emailToken);
    }
}
