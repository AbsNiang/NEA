package com.example.demo.Database;

import com.example.demo.Objects.Transaction;

import java.sql.*;
import java.util.ArrayList;

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

    public static double sumTransactions(String emailAddress, boolean forOnePerson) {
        Connection connection = null;
        PreparedStatement psSum = null;
        ResultSet rs = null;
        double sum = 0;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            String whereClause;
            if (forOnePerson) {
                whereClause = "WHERE EmailAddress = ?";
            } else {
                whereClause = "";
            }
            psSum = connection.prepareStatement("SELECT SUM(MoneySpent) AS sumPrice FROM Transactions " + whereClause);
            if (forOnePerson) {
                psSum.setString(1, emailAddress);
            }
            rs = psSum.executeQuery();
            if (rs.next()) {
                sum = rs.getDouble("sumPrice");
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

    public static ArrayList<Transaction> getTransactionsList() {
        ArrayList<Transaction> listData = new ArrayList<>();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT * FROM Transactions");
            resultSet = prepare.executeQuery();
            Transaction transaction;
            while (resultSet.next()) {
                transaction = new Transaction(resultSet.getString("EmailAddress"), resultSet.getDouble("MoneySpent"), resultSet.getDate("Date").toLocalDate(), resultSet.getString("Time"));
                listData.add(transaction);
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
            if (prepare != null) {
                try {
                    prepare.close();
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
        return listData;
    }
}
