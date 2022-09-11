package com.example.demo.auth.Controllers;

import com.example.demo.DBUtils.UserTable;
import com.example.demo.SceneHandler;
import com.example.demo.auth.Objects.User;
import com.example.demo.auth.Registration.PasswordConverter;
import com.example.demo.auth.Registration.PasswordHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Region;


import java.net.URL;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {

    @FXML
    private PasswordField pf_newpassword;
    @FXML
    private PasswordField pf_confirmpassword;
    @FXML
    private Button btn_complete;

    private String email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_complete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (pf_newpassword.getText().equals(pf_confirmpassword.getText()) && PasswordHandler.passwordCheck(pf_newpassword.getText())) {
                    byte[] salt = PasswordHandler.generateSalt();
                    byte[] byteDigestPassword = PasswordHandler.getSaltedHash(pf_newpassword.getText(), salt);
                    String hashedPassword = PasswordConverter.toHex(byteDigestPassword);
                    String strSalt = PasswordConverter.toHex(salt);
                    User user = new User(email, hashedPassword, strSalt, "","",false,false);
                    System.out.println(user.getEmailAddress());
                    UserTable.alterPassword(user);
                    SceneHandler.changeScene(actionEvent, "AdminLoggedIn.fxml", "Welcome!", email, 1100, 651);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.setContentText("Both passwords need to be identical " +
                            "and contain at least 8 characters including upper and lowercase, 1 number, and 1 special character.");
                    alert.show();
                }
            }
        });
    }

    public void setEmail(String emailAddress) {
        email = emailAddress;
    }
}
