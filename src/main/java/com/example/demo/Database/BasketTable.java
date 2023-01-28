package com.example.demo.Database;

import com.example.demo.auth.Objects.Item;
import javafx.scene.control.Alert;

import java.sql.*;

//customer should be able to search through their old purchased baskets
public class BasketTable {
    public static final String dbLocation = (System.getProperty("user.dir") + "\\databaseNEA.accdb");

    public static void createBasket(String emailAddress) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psInsert = connection.prepareStatement("INSERT INTO Basket (EmailAddress, Purchased) VALUES (?, ?)");
            psInsert.setString(1, emailAddress);
            psInsert.setBoolean(2, false);
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

    public static int returnBasketID() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int info = -1;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            preparedStatement = connection.prepareStatement("SELECT MAX(BasketID) FROM Basket");
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No basket has been created");
            } else {
                while (resultSet.next()) {
                    info = resultSet.getInt("BasketID");
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
}
