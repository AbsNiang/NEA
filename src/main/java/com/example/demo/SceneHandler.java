package com.example.demo;

import com.example.demo.DBUtils.UserTable;
import com.example.demo.auth.Controllers.LoggedInController;
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
        if (emailAddress != null && fxmlFile.equals("LoggedIn.fxml")) {
            try {
                FXMLLoader loader = new FXMLLoader(SceneHandler.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(emailAddress, null);
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
        stage.show();
    }
}
