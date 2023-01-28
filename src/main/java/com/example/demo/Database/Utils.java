package com.example.demo.Database;

import com.example.demo.auth.Objects.User;
import javafx.scene.control.Alert;

import java.sql.*;

public class Utils {

    public static final String dbLocation = (System.getProperty("user.dir") + "\\databaseNEA.accdb");

    public static void updateInfo(String primaryKey, String field, String newInfo, String tableName, String primaryKeyName) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psInsert = connection.prepareStatement("UPDATE " + tableName + " SET " + field + " = '" + newInfo + "' WHERE " + primaryKeyName + " = '" + primaryKey + "'");
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

    public static String selectFromRecord(String wantedField, String tableName, String primaryKeyName, String primaryKey) {
        String content = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            ps = connection.prepareStatement("SELECT " + wantedField + " FROM " + tableName + " WHERE " + primaryKeyName + " = ?");
            ps.setString(1, primaryKey);
            resultSet = ps.executeQuery();
            if (!resultSet.isBeforeFirst()) { //if item doesn't exist
                System.out.println("Item doesn't exists.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("An item with this name already exists.");
                alert.show();
            } else {
                while (resultSet.next()) {
                    content = resultSet.getString(wantedField);
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
