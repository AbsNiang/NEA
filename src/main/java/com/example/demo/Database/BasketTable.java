package com.example.demo.Database;

import com.example.demo.auth.Objects.Item;
import javafx.scene.control.Alert;

import java.sql.*;

//customer should be able to search through their old purchased baskets
public class BasketTable {
    public static final String dbLocation = (System.getProperty("user.dir") + "\\databaseNEA.accdb");

    public static int createBasket(String emailAddress) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        int primaryKey = -1;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psInsert = connection.prepareStatement("INSERT INTO Basket (EmailAddress, Purchased) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            psInsert.setString(1, emailAddress);
            psInsert.setBoolean(2, false);
            psInsert.executeUpdate();
            System.out.println("Successfully created a basket.");
            ResultSet rs = psInsert.getGeneratedKeys();
            if (rs != null && rs.next()) {
                primaryKey = rs.getInt(1);
                System.out.println(primaryKey);
            }
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
        return primaryKey;
    }
}
