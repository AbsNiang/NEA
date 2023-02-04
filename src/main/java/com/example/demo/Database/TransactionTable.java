package com.example.demo.Database;

import com.example.demo.Objects.Transaction;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.demo.Database.Utils.dbLocation;

public class TransactionTable {
    public static void addTransaction(Transaction transaction) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psInsert = connection.prepareStatement("INSERT INTO Transactions (EmailAddress, MoneySpent, Date, Time) VALUES (?, ?, ?, ?)");
            psInsert.setString(1, transaction.getEmailAddress());
            psInsert.setDouble(2, transaction.getMoneySpent());
            //could use LocalDate/LocalTime.now(), but using object eliminates discrepancies
            psInsert.setDate(3, (Date.valueOf(transaction.getDateOfTransaction())));
            psInsert.setTime(4, (Time.valueOf(transaction.getTimeOfTransaction())));
            psInsert.executeUpdate();
            System.out.println("Transaction has been added.");
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
