package com.example.demo.Database;

import com.example.demo.Objects.BasketItem;

import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

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
            while (resultSet.next()) {
                double price = resultSet.getDouble("Price");
                int quantity = resultSet.getInt("QuantityAdded");
                content = price * quantity;
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

    public static double sumItems(int basketID) {
        double orderTotal = 0;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            ps = connection.prepareStatement("SELECT Price, QuantityAdded " +
                    "FROM BasketItem, Items " +
                    "WHERE BasketItem.ItemName = Items.ItemName AND BasketID = ?");
            ps.setInt(1, basketID);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                double price = resultSet.getDouble("Price");
                int quantity = resultSet.getInt("QuantityAdded");
                orderTotal += price * quantity;
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
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(orderTotal));
    }

    public static ArrayList<BasketItem> createBasketItemList(int basketID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<BasketItem> basketItemList = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            preparedStatement = connection.prepareStatement("SELECT ItemName, QuantityAdded FROM BasketItem WHERE BasketID = ?");
            preparedStatement.setInt(1, basketID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                basketItemList.add(new BasketItem(resultSet.getString("ItemName"), resultSet.getInt("QuantityAdded")));
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
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
        return basketItemList;
    }

    public static void deleteRecord(int basketID, String itemName) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            ps = connection.prepareStatement("DELETE FROM BasketItem WHERE BasketID = ? AND ItemName = ?");
            ps.setInt(1, basketID);
            ps.setString(2, itemName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
    }

    public static HashMap<String, Integer> getBasketItemTotalOrderedList() {
        HashMap<String, Integer> listData = new HashMap<>();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT ItemName, QuantityAdded FROM BasketItem, Basket WHERE BasketItem.BasketID = Basket.BasketID AND Purchased = true");
            resultSet = prepare.executeQuery();
            while (resultSet.next()) {
                String itemName = resultSet.getString("ItemName");
                int itemQuantityAdded = resultSet.getInt("QuantityAdded");
                listData.merge(itemName, itemQuantityAdded, Integer::sum); // adds current quantity to existing one in hashmap.
            }
        } catch (Exception e) {
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
        return  listData;
    }
}
