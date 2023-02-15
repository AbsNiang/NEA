package com.example.demo.Database;

import com.example.demo.General.Repository;
import com.example.demo.Objects.Item;
import javafx.scene.control.Alert;

import java.sql.*;

import static com.example.demo.Database.Utils.dbLocation;

public class ItemTable {

    public static void insertItem(Item item) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckItemExists = null;
        ResultSet resultSet = null;

        try {

            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psCheckItemExists = connection.prepareStatement("SELECT * FROM Items WHERE ItemName = ? ");
            psCheckItemExists.setString(1, item.getName());
            resultSet = psCheckItemExists.executeQuery();
            if (resultSet.isBeforeFirst()) { //if true, item already exists.
                System.out.println("Item already exists.");
                Repository.giveAlert("An item with this name already exists.","error");

            } else {
                psInsert = connection.prepareStatement("INSERT INTO Items (ItemName, Price, Quantity, Tags, Description) VALUES (?, ?, ?, ?, ?)");
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

    //Removes the amount added to the basket from the Item in DB (-ve if basket isn't purchased and app is closed)
    public static void updateItemAmount(String itemName, int amount) {
        int previousQuantity = Integer.parseInt(Utils.selectFromRecord("Quantity", "Items", "ItemName", itemName));
        Utils.updateInfo(itemName, "Quantity", Integer.toString(previousQuantity - amount), "Items", "ItemName");
    }

    public static int sumItems() {
        Connection connection = null;
        PreparedStatement psSum = null;
        ResultSet rs = null;
        int sum = 0;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psSum = connection.prepareStatement("SELECT SUM(Quantity) AS sumPrice FROM Items");
            rs = psSum.executeQuery();
            if (rs.next()) {
                sum = rs.getInt("sumPrice");
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
            if (psSum != null) {
                try {
                    psSum.close();
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
        return sum;
    }

    public static int countItems() {
        Connection connection = null;
        PreparedStatement psCount = null;
        ResultSet rs = null;
        int count = 0;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psCount = connection.prepareStatement("SELECT COUNT(*) AS itemTotal FROM Items ");
            rs = psCount.executeQuery();
            if (rs.next()) {
                count = rs.getInt("itemTotal");
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

