package com.example.demo.DBUtils;

import com.example.demo.auth.Objects.Item;
import javafx.scene.control.Alert;

import java.sql.*;

public class ItemTable {
    private static final String dbLocation = (System.getProperty("user.dir") + "\\databaseNEA.accdb");

    public static void insertItem(Item item){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckItemExists = null;
        ResultSet resultSet = null;

        try {

            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psCheckItemExists = connection.prepareStatement("SELECT * FROM Items WHERE Name = ? ");
            psCheckItemExists.setString(1, item.getName());
            resultSet = psCheckItemExists.executeQuery();
            if (resultSet.isBeforeFirst()) { //if true, item already exists.
                System.out.println("Item already exists.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("An item with this name and price already exists.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO Items (Name, Price, Quantity, Tags, Description) VALUES (?, ?, ?, ?, ?)");
                psInsert.setString(1, item.getName());
                psInsert.setDouble(2, item.getCost());
                psInsert.setInt(3, item.getQuantity());
                psInsert.setString(4, item.getTags());
                psInsert.setString(5, item.getDescription());
                psInsert.executeUpdate();
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
            if (psCheckItemExists != null) {
                try {
                    psCheckItemExists.close();
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
}
