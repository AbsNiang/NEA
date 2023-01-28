package com.example.demo;

import com.example.demo.auth.Controllers.AdminLoggedInController;
import com.example.demo.auth.Controllers.CustomerLoggedInController;
import com.example.demo.auth.Controllers.ResetPasswordController;
import com.example.demo.auth.Objects.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneHandler {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String emailAddress, int width, int height) {
        Parent root = null;
        if ((emailAddress != null)) {
            try {
                FXMLLoader loader = new FXMLLoader(SceneHandler.class.getResource(fxmlFile));
                root = loader.load();
                switch (fxmlFile){
                    case "CustomerLoggedIn.fxml":
                        CustomerLoggedInController customerLoggedInController = loader.getController();
                        customerLoggedInController.setCustomerEmail(emailAddress);
                        break;
                    case "ResetPassword.fxml":
                        ResetPasswordController resetPasswordController = loader.getController();
                        resetPasswordController.setEmail(emailAddress);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(SceneHandler.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.centerOnScreen();
        stage.show();
    }
}
