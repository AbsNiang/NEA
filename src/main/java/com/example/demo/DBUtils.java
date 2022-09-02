package com.example.demo;

import com.example.demo.auth.LoggedInController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

// the first signed-up user will be the owner, and they will be able to edit to add other owners or admins.
public class DBUtils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String emailAddress) {
        Parent root = null;
        if (emailAddress != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(emailAddress, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String emailAddress, String password, String firstName, String Surname, boolean hasLoyaltyCard) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nea-db", "root", "toor");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE emailAddress = ?");
            psCheckUserExists.setString(1, emailAddress);
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) { //if true, username is taken, if false, it means it is available.
                System.out.println("User already exists.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (emailAddress, password, firstName, Surname, hasLoyaltyCard) VALUES (?, ?, ?, ?, ?)");
                psInsert.setString(1, emailAddress);
                psInsert.setString(2, password);
                psInsert.setString(3, firstName);
                psInsert.setString(4, Surname);
                psInsert.setBoolean(5, hasLoyaltyCard);
                psInsert.executeUpdate();
                changeScene(event, "LoggedIn.fxml", "Welcome!", emailAddress);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void logInUser(ActionEvent event, String emailAddress, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nea-db", "root", "toor");
            preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE emailAddress = ?");
            preparedStatement.setString(1, emailAddress);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()){
                System.out.println("User not found.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect.");
                alert.show();
            }else{
                while (resultSet.next()){
                    String retrievePassword = resultSet.getString("password");
                    if (retrievePassword.equals(password)){
                        changeScene(event, "LoggedIn.fxml", "Welcome!", emailAddress);
                    }else {
                        System.out.println("Passwords didn't match.");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrrect.");
                        alert.show();
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
