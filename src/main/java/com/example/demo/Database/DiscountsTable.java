package com.example.demo.Database;

import javafx.scene.control.Alert;

import java.sql.*;

import static com.example.demo.Database.Utils.dbLocation;

public class DiscountsTable {

    public static void giveUserDiscount(String customerEmail) {
        double customerTransactionSum = TransactionTable.sumTransactionsForCustomer(customerEmail);
        double thresholdPassed = 0;
        if (customerTransactionSum > 10) { //all possible discount thresholds
            if (customerTransactionSum > 15) {
                if (customerTransactionSum > 20) {
                    if (customerTransactionSum > 30) {
                        if (customerTransactionSum > 40) {
                            if (customerTransactionSum > 50) {
                                if (customerTransactionSum > 70) {
                                    if (customerTransactionSum > 100) {
                                        thresholdPassed = 100;
                                    } else {
                                        thresholdPassed = 70;
                                    }
                                } else {
                                    thresholdPassed = 50;
                                }
                            } else {
                                thresholdPassed = 40;
                            }
                        } else {
                            thresholdPassed = 30;
                        }
                    } else {
                        thresholdPassed = 20;
                    }
                } else {
                    thresholdPassed = 15;
                }
            } else {
                thresholdPassed = 10;
            }
        }
        System.out.println(thresholdPassed);
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckDiscountExists = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            psCheckDiscountExists = connection.prepareStatement("SELECT PercentageOff FROM UserDiscountLink, Discounts " +
                    "WHERE EmailAddress = ? AND ThresholdSpend = ? AND UserDiscountLink.PercentageOff = Discounts.PercentageOff");
            psCheckDiscountExists.setString(1, customerEmail);
            psCheckDiscountExists.setDouble(2, thresholdPassed);
            resultSet = psCheckDiscountExists.executeQuery();
            if (resultSet.isBeforeFirst()) { //if true, discount already exists.
                System.out.println("User already has max discount applied");

            } else {
                connection.close();
                connection =  DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
                PreparedStatement ps = connection.prepareStatement("SELECT PercentageOff FROM Discounts WHERE ThresholdSpend = ?");
                ps.setDouble(1, thresholdPassed);
                ResultSet rs = ps.executeQuery();
                int percentageOff = 0;
                while (rs.next()) {
                    percentageOff = rs.getInt("PercentageOff");
                }
                connection.close();
                connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
                if (percentageOff > 0) {
                    psInsert = connection.prepareStatement("INSERT INTO UserDiscountLink (PercentageOff, EmailAddress) VALUES (?, ?)");
                    psInsert.setInt(1, percentageOff);
                    psInsert.setString(2, customerEmail);
                    psInsert.executeUpdate();
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
            if (psCheckDiscountExists != null) {
                try {
                    psCheckDiscountExists.close();
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
