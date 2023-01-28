package com.example.demo.Database;

import javafx.scene.control.Alert;

import java.sql.*;

public class BasketItemTable {
    public static final String dbLocation = (System.getProperty("user.dir") + "\\databaseNEA.accdb");

    public static void addItemToBasket(String itemName, int basketID, int QuantityToAdd) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckItemAlreadyInBasket;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psCheckItemAlreadyInBasket = connection.prepareStatement("SELECT * FROM Items WHERE ItemName = ? ");
            psCheckItemAlreadyInBasket.setString(1, itemName);
            resultSet = psCheckItemAlreadyInBasket.executeQuery();
            if (resultSet.isBeforeFirst()) { //if true, item already exists.
                System.out.println("Item already in basket. " +
                        "\nItem quantity added to quantity in basket.");
            } else {
                psInsert = connection.prepareStatement("INSERT INTO BasketItem (ItemName, BasketID, QuantityAdded) VALUES (?, ?, ?)");
                psInsert.setString(1, itemName);
                psInsert.setInt(2, basketID);
                psInsert.setInt(3, QuantityToAdd);
                psInsert.executeUpdate();
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
