package com.example.demo.Database;

import javafx.scene.control.Alert;

import java.sql.*;

import static com.example.demo.Database.Utils.dbLocation;

public class BasketItemTable {


    public static void addItemToBasket(String itemName, int basketID, int QuantityToAdd) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckItemAlreadyInBasket;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psCheckItemAlreadyInBasket = connection.prepareStatement("SELECT * FROM BasketItem WHERE ItemName = ? AND BasketID = ?");
            psCheckItemAlreadyInBasket.setString(1, itemName);
            psCheckItemAlreadyInBasket.setInt(2, basketID);
            resultSet = psCheckItemAlreadyInBasket.executeQuery();
            if (resultSet.next()) { //if true, item already exists.
                int quantityInDatabase = resultSet.getInt(4);
                System.out.println("Item already in basket. " +
                        "\nItem quantity needs to be added to quantity in basket.");
                Utils.updateInfo(itemName, "QuantityAdded", Integer.toString(QuantityToAdd + quantityInDatabase), "BasketItem", "ItemName");//doesn't need to be the primary key
                System.out.println("Incremented quantity in database.");
            } else {
                psInsert = connection.prepareStatement("INSERT INTO BasketItem (ItemName, BasketID, QuantityAdded) VALUES (?, ?, ?)");
                psInsert.setString(1, itemName);
                psInsert.setInt(2, basketID);
                psInsert.setInt(3, QuantityToAdd);
                psInsert.executeUpdate();
                System.out.println("Item added to basket");
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

    public static double fetchTotalPriceForItems(String itemName, int basketID) {
        double content = -1;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            ps = connection.prepareStatement("SELECT Price, QuantityAdded " +
                    "FROM BasketItem, Items " +
                    "WHERE ItemName = ? AND BasketItem.ItemName = Items.ItemName AND BasketID = ?");
            ps.setString(1, itemName);
            ps.setInt(2, basketID);
            resultSet = ps.executeQuery();
            if (!resultSet.isBeforeFirst()) { //if item doesn't exist
                System.out.println("Item doesn't exists.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("An item with this name already exists.");
                alert.show();
            } else {
                while (resultSet.next()) {
                    double price = resultSet.getDouble("Price");
                    int quantity = resultSet.getInt("QuantityAdded");
                    content = price * quantity;
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
            if (ps != null) {
                try {
                    ps.close();
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
        return content;
    }
}
