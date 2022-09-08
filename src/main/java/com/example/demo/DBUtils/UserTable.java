package com.example.demo.DBUtils;

import com.example.demo.SceneHandler;
import com.example.demo.auth.Objects.User;
import com.example.demo.auth.Registration.PasswordConverter;
import com.example.demo.auth.Registration.PasswordHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.Arrays;

// the first signed-up user will be the owner, and they will be able to edit to add other owners or admins.
public class UserTable {

    public static void signUpUser(ActionEvent event, User user) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nea-db", "root", "toor");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE emailAddress = ?");
            psCheckUserExists.setString(1, user.getEmailAddress());
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) { //if true, username is taken, if false, it means it is available.
                System.out.println("User already exists.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                if (resultSet.next()) {
                    user.setOwner(true);
                    System.out.println("User set as owner");
                }
                psInsert = connection.prepareStatement("INSERT INTO users (emailAddress, password, passwordSalt, firstName, Surname, hasLoyaltyCard, isOwner) VALUES (?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, user.getEmailAddress());
                psInsert.setString(2, user.getPassword());
                psInsert.setString(3, user.getPasswordSalt());
                psInsert.setString(4, user.getFirstName());
                psInsert.setString(5, user.getSurname());
                psInsert.setBoolean(6, user.isHasLoyaltyCard());
                psInsert.setBoolean(7, user.isOwner());
                psInsert.executeUpdate();
                SceneHandler.changeScene(event, "LoggedIn.fxml", "Welcome!", user.getEmailAddress(), 900, 600);
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

    public static void logInUser(ActionEvent event, String emailAddress, String password) {
        String correctPassword = fetchInfo(emailAddress, "password");
        String originalSalt = fetchInfo(emailAddress, "passwordSalt");
        byte[] byteSalt = PasswordConverter.fromHex(originalSalt);
        byte[] loginPassword = PasswordHandler.getSaltedHash(password, byteSalt);
        byte[] storedPassword = PasswordConverter.fromHex(correctPassword);

        if (Arrays.equals(loginPassword, storedPassword)) {
            System.out.println("Passwords are a match.");
            SceneHandler.changeScene(event, "LoggedIn.fxml", "Welcome!", emailAddress, 900, 600);
        } else {
            System.out.println("Passwords didn't match.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The provided credentials are incorrect.");
            alert.show();
        }
    }

    private static String fetchInfo(String emailAddress, String desiredField) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String info = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nea-db", "root", "toor");
            preparedStatement = connection.prepareStatement("SELECT " + desiredField + " FROM users WHERE emailAddress = ?");
            preparedStatement.setString(1, emailAddress);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect.");
                alert.show();
            } else {
                while (resultSet.next()) {
                    info = resultSet.getString(desiredField);
                    System.out.println(info);
                }
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
        return info;
    }

    public static boolean checkUserExists(String email) {
        Connection connection = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nea-db", "root", "toor");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE emailAddress = ?");
            psCheckUserExists.setString(1, email);
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) { //if true, username is taken, if false, it means it is available.
                System.out.println("User exists.");
                return true;
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void alterPassword(User user) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nea-db", "root", "toor");
            psInsert = connection.prepareStatement("UPDATE users SET password = '"+user.getPassword()+"', passwordSalt = '"+user.getPasswordSalt()+"' WHERE emailAddress = '"+user.getEmailAddress()+"'");
            psInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
}
