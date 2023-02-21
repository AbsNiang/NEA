package com.example.demo.Database;

import com.example.demo.General.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class Utils {

    public static final String dbLocation = (System.getProperty("user.dir") + "\\databaseNEA.accdb");

    public static void updateInfo(String primaryKey, String field, String newInfo, String tableName, String primaryKeyName) {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psUpdate = connection.prepareStatement("UPDATE " + tableName + " SET " + field + " = '" + newInfo + "' WHERE " + primaryKeyName + " = '" + primaryKey + "'");
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psUpdate != null) {
                try {
                    psUpdate.close();
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
                System.out.println("Item doesn't exist.");
                Repository.giveAlert("Item doesn't exist.", "error");
            } else {
                while (resultSet.next()) {
                    content = resultSet.getString(wantedField);
                    System.out.println("Content from wanted field is: " + content);
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

    public static void changeAutoStockUpSetting(boolean autoStockUp) {//whether the admin wants the program to automatically order stock
        Path path = Paths.get("src/main/resources/com/example/demo/AutoStockUp");
        try {
            Files.writeString(path, Boolean.toString(autoStockUp), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean readAutoStockUpSetting() {
        Path path = Paths.get("src/main/resources/com/example/demo/AutoStockUp");
        boolean setting = false;
        try {
            setting = Boolean.parseBoolean(Files.readString(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return setting;
    }

}
