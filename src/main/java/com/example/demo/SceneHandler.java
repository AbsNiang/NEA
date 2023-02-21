package com.example.demo;

import com.example.demo.Controllers.AdminLoggedInController;
import com.example.demo.Controllers.CustomerLoggedInController;
import com.example.demo.Controllers.ResetPasswordController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHandler {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String emailAddress, int width, int height) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(SceneHandler.class.getResource(fxmlFile));
            root = loader.load();
            if ((emailAddress != null)) {
                switch (fxmlFile) {
                    case "CustomerLoggedIn.fxml" -> {
                        CustomerLoggedInController customerLoggedInController = loader.getController();
                        customerLoggedInController.setCustomerEmail(emailAddress);
                    }
                    case "AdminLoggedIn.fxml" -> {
                        AdminLoggedInController adminLoggedInController = loader.getController();
                        adminLoggedInController.setAdminEmail(emailAddress);
                    }
                    case "ResetPassword.fxml" -> {
                        ResetPasswordController resetPasswordController = loader.getController();
                        resetPasswordController.setEmail(emailAddress);
                    }
                }
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.centerOnScreen();
            stage.show();
            stage.setOnCloseRequest(windowEvent -> {
                try {
                    CustomerLoggedInController customerLoggedInController = loader.getController();
                    customerLoggedInController.closeCustomerWindow();
                } catch (Exception e) {
                    System.out.println("not in customer fxml file.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
