package com.example.demo.Database;

import com.example.demo.General.Repository;
import com.example.demo.Objects.User;
import com.example.demo.Registration.PasswordConverter;
import com.example.demo.Registration.PasswordHandler;
import com.example.demo.SceneHandler;
import javafx.event.ActionEvent;

import java.sql.*;
import java.util.Arrays;

import static com.example.demo.Database.Utils.dbLocation;

// the first signed-up user will be the owner, and they will be able to edit to add other owners or admins.
public class UserTable {

    public static void signUpUser(ActionEvent event, User user) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {

            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM Users WHERE emailAddress = ?");
            psCheckUserExists.setString(1, user.getEmailAddress());
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) { //if true, username is taken, if false, it means it is available.
                System.out.println("User already exists.");
                Repository.giveAlert("You cannot use this email address.", "error");

            } else {
                psInsert = connection.prepareStatement("INSERT INTO Users (emailAddress, password, passwordSalt, firstName, Surname, isAdmin) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, user.getEmailAddress());
                psInsert.setString(2, user.getPassword());
                psInsert.setString(3, user.getPasswordSalt());
                psInsert.setString(4, user.getFirstName());
                psInsert.setString(5, user.getSurname());
                psInsert.setBoolean(6, user.isAdmin());
                psInsert.executeUpdate();
                SceneHandler.changeScene(event, "CustomerLoggedIn.fxml", "Welcome!", user.getEmailAddress(), 1100, 651);
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
        try {
            String correctPassword = fetchInfo(emailAddress, "password");
            System.out.println("correct:" + correctPassword);
            String originalSalt = fetchInfo(emailAddress, "passwordSalt");
            String isAdmin = fetchInfo(emailAddress, "isAdmin");
            System.out.println("Admin? = " + isAdmin);
            byte[] byteSalt = PasswordConverter.fromHex(originalSalt);
            byte[] loginPassword = PasswordHandler.getSaltedHash(password, byteSalt);
            byte[] storedPassword = PasswordConverter.fromHex(correctPassword);
            if (Arrays.equals(loginPassword, storedPassword)) {
                System.out.println("Passwords are a match.");
                if (isAdmin.equals("TRUE")) {
                    SceneHandler.changeScene(event, "AdminLoggedIn.fxml", "Welcome!", emailAddress, 1100, 651);
                } else {
                    SceneHandler.changeScene(event, "CustomerLoggedIn.fxml", "Welcome!", emailAddress, 1100, 651);
                }

            } else {
                System.out.println("Passwords didn't match.");
                Repository.giveAlert("The provided credentials are incorrect.", "error");

            }
        } catch (Exception e) {
            System.out.println("User doesn't exist");
        }
    }

    private static String fetchInfo(String emailAddress, String desiredField) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String info = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            preparedStatement = connection.prepareStatement("SELECT " + desiredField + " FROM users WHERE emailAddress = ?");
            preparedStatement.setString(1, emailAddress);
            resultSet = preparedStatement.executeQuery();
            if (!checkUserExists(emailAddress)) {
                System.out.println("User not found.");
                Repository.giveAlert("Provided credentials are incorrect.", "error");

            } else {
                while (resultSet.next()) {
                    info = resultSet.getString(desiredField);
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
        PreparedStatement prepare = null;
        ResultSet resultSet = null;
        int numbOfUsers = countUsers();
        String[] emailAddresses = new String[numbOfUsers];
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT EmailAddress FROM Users");
            resultSet = prepare.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                emailAddresses[i] = resultSet.getString("EmailAddress");
                i++;
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
            if (prepare != null) {
                try {
                    prepare.close();
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
        Repository.mergeSort(emailAddresses);
        System.out.println("Ordered List: " + Arrays.toString(emailAddresses));
        return Repository.binarySearch(email, emailAddresses);
    }

    public static void alterPassword(User user) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psInsert = connection.prepareStatement("UPDATE Users SET password = '" + user.getPassword() + "', passwordSalt = '" + user.getPasswordSalt() + "' WHERE emailAddress = '" + user.getEmailAddress() + "'");
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

    public static void updateBooleanInfo(String email, String field, boolean newInfo) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psInsert = connection.prepareStatement("UPDATE Users SET " + field + " = '" + newInfo + "' WHERE emailAddress = '" + email + "'");
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

    public static void deleteUser(String emailAddress) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psInsert = connection.prepareStatement("DELETE FROM Users WHERE emailAddress = '" + emailAddress + "'");
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

    public static int countUsers() {
        Connection connection = null;
        PreparedStatement psCount = null;
        ResultSet rs = null;
        int count = 0;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psCount = connection.prepareStatement("SELECT COUNT(*) AS userTotal FROM Users ");
            rs = psCount.executeQuery();
            if (rs.next()) {
                count = rs.getInt("userTotal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCount != null) {
                try {
                    psCount.close();
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
        return count;
    }
}
