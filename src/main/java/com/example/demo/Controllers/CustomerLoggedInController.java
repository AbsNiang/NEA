package com.example.demo.Controllers;

import com.example.demo.Database.*;
import com.example.demo.EmailHandling.Email;
import com.example.demo.EmailHandling.EmailToken;
import com.example.demo.General.Repository;
import com.example.demo.Objects.BasketItem;
import com.example.demo.Objects.Discount;
import com.example.demo.Objects.Item;
import com.example.demo.Objects.Transaction;
import com.example.demo.SceneHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.math.RoundingMode;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.demo.Database.Utils.dbLocation;

public class CustomerLoggedInController implements Initializable {

    //Side Controls
    @FXML
    private Button btn_browse;
    @FXML
    private Button btn_discounts;
    @FXML
    private Button btn_basket;
    @FXML
    private Button btn_signout;

    //Browse Anchor Pane:
    @FXML
    private AnchorPane browse_form;
    @FXML
    private Button btn_plus1;
    @FXML
    private Button btn_minus1;
    @FXML
    private Button btn_addToBasket;
    @FXML
    private Label lbl_itemName;
    @FXML
    private Label lbl_itemPrice;
    @FXML
    private Label lbl_itemQuantity;
    @FXML
    private Label lbl_itemAmount;
    @FXML
    private Label lbl_itemTotal;
    @FXML
    private Label lbl_itemDescription;
    @FXML
    private TableView<Item> tv_items;
    @FXML
    private TableColumn<Item, String> tvCol_itemName;
    @FXML
    private TableColumn<Item, String> tvCol_itemPrice;
    @FXML
    private TableColumn<Item, String> tvCol_itemQuantity;
    @FXML
    private TableColumn<Item, String> tvCol_itemTags;
    @FXML
    private TableColumn<Item, String> tvCol_itemDescription;

    //discounts Anchor Pane:
    @FXML
    private AnchorPane discounts_form;
    @FXML
    private TableView<Discount> tv_discounts; //discounts that are available to user
    @FXML
    private TableColumn<Discount, String> tvCol_discountsPercentageOff;
    @FXML
    private TableColumn<Discount, String> tvCol_discountsThresholdSpend;


    //Basket Anchor Pane:
    @FXML
    private AnchorPane basket_form;
    @FXML
    private Label lbl_basketTotalOrderCost;
    @FXML
    private Label lbl_basketAdjustedOrderCost;
    @FXML //BasketItem Table
    private TableView<BasketItem> tv_basketItem;
    @FXML
    private TableColumn<BasketItem, String> tvCol_basketItemName;
    @FXML
    private TableColumn<BasketItem, String> tvCol_basketItemQuantityAdded;
    @FXML
    private Label lbl_basketItemName;
    @FXML
    private Label lbl_basketItemQuantity;
    @FXML
    private Label lbl_basketItemTotalCost;
    @FXML
    private Button btn_removeItemFromBasket;
    @FXML
    private Button btn_purchase;
    @FXML
    private Button btn_viewPreviousBasket;
    @FXML //Discount Table
    private TableView<Discount> tv_basketDiscounts;
    @FXML
    private TableColumn<Discount, Integer> tvCol_basketDiscountPercentageOff;
    @FXML
    private TableColumn<Discount, Double> tvCol_basketDiscountThreshold;
    @FXML
    private Label lbl_basketPercentageOff;


    private boolean basketMade = false;
    private String customerEmail;
    private int basketID;
    private int oldBasketID;

    //Item TableView Stuff
    private ObservableList<Item> addItemList() {
        ObservableList<Item> listData = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT * FROM ITEMS");
            resultSet = prepare.executeQuery();
            Item item;
            while (resultSet.next()) {
                item = new Item(resultSet.getString("ItemName"),
                        resultSet.getDouble("Price"),
                        resultSet.getInt("Quantity"),
                        resultSet.getString("Tags"),
                        resultSet.getString("Description"));
                listData.add(item);
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

    private void showItemList() {
        ObservableList<Item> listAddItem = addItemList();
        tvCol_itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tvCol_itemPrice.setCellValueFactory(new PropertyValueFactory<>("cost"));
        tvCol_itemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tvCol_itemTags.setCellValueFactory(new PropertyValueFactory<>("tags"));
        tvCol_itemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tv_items.setItems(listAddItem);
    }

    private void selectAddItemList() {
        Item item = tv_items.getSelectionModel().getSelectedItem();
        lbl_itemName.setText(item.getName());
        lbl_itemPrice.setText(Double.toString(item.getCost()));
        lbl_itemQuantity.setText(Integer.toString(item.getQuantity()));
        lbl_itemDescription.setText(item.getDescription());
    }

    //BasketItem TableView Stuff
    private ObservableList<BasketItem> basketItemList() {
        ObservableList<BasketItem> listData = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT * FROM BasketItem WHERE BasketID = ?");
            prepare.setInt(1, basketID);
            resultSet = prepare.executeQuery();
            BasketItem basketItem;
            while (resultSet.next()) {
                basketItem = new BasketItem(resultSet.getString("ItemName"), resultSet.getInt("QuantityAdded"));
                listData.add(basketItem);
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

    private ObservableList<BasketItem> oldBasketItemList() {
        ObservableList<BasketItem> listData = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT BasketID FROM Basket WHERE EmailAddress = ? AND Purchased = ?");
            prepare.setString(1, customerEmail);
            prepare.setBoolean(2, true);
            resultSet = prepare.executeQuery();
            int previousBasketID = 0;
            while (resultSet.next()) {
                previousBasketID = resultSet.getInt("BasketID");


            }
            oldBasketID = previousBasketID;
            BasketItem basketItem;
            connection.close();
            prepare.close();
            resultSet.close();
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT * FROM BasketItem WHERE BasketID = ?");
            prepare.setInt(1, oldBasketID);
            resultSet = prepare.executeQuery();
            while (resultSet.next()) {
                basketItem = new BasketItem(resultSet.getString("ItemName"), resultSet.getInt("QuantityAdded"));
                listData.add(basketItem);

            }
        } catch (SQLException e) {
            Repository.giveAlert("No previous basket.", "error");
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

    private void showBasketItemList() {
        ObservableList<BasketItem> listAddItem = basketItemList();
        tvCol_basketItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tvCol_basketItemQuantityAdded.setCellValueFactory(new PropertyValueFactory<>("quantityAdded"));
        tv_basketItem.setItems(listAddItem);
    }

    private void showOldBasketItemList() {
        ObservableList<BasketItem> listAddItem = oldBasketItemList();
        tvCol_basketItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tvCol_basketItemQuantityAdded.setCellValueFactory(new PropertyValueFactory<>("quantityAdded"));
        tv_basketItem.setItems(listAddItem);
    }

    private void selectBasketItemList() {
        BasketItem basketItem = tv_basketItem.getSelectionModel().getSelectedItem();
        lbl_basketItemName.setText(basketItem.getItemName());
        lbl_basketItemQuantity.setText(Integer.toString(basketItem.getQuantityAdded()));
    }

    private ObservableList<Discount> discountList() {
        ObservableList<Discount> listData = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT PercentageOff, ThresholdSpend FROM Discounts, UserDiscountLink " +
                    "WHERE EmailAddress = ? AND UserDiscountLink.PercentageOff = Discounts.PercentageOff");
            prepare.setString(1, customerEmail);
            resultSet = prepare.executeQuery();
            Discount discount;
            while (resultSet.next()) {
                discount = new Discount(resultSet.getInt("PercentageOff"), resultSet.getDouble("ThresholdSpend"));
                listData.add(discount);
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

    private void showDiscountList() {
        ObservableList<Discount> listDiscounts = discountList();
        tvCol_discountsPercentageOff.setCellValueFactory(new PropertyValueFactory<>("percentageOff"));
        tvCol_discountsThresholdSpend.setCellValueFactory(new PropertyValueFactory<>("purchaseThreshold"));
        tv_discounts.setItems(listDiscounts);
    }

    private void showBasketDiscountList() {
        ObservableList<Discount> listDiscounts = discountList();
        tvCol_basketDiscountPercentageOff.setCellValueFactory(new PropertyValueFactory<>("percentageOff"));
        tvCol_basketDiscountThreshold.setCellValueFactory(new PropertyValueFactory<>("purchaseThreshold"));
        tv_basketDiscounts.setItems(listDiscounts);
    }

    private void selectBasketDiscountList() {
        try {
            Discount discount = tv_basketDiscounts.getSelectionModel().getSelectedItem();
            lbl_basketPercentageOff.setText(Integer.toString(discount.getPercentageOff()));
        } catch (NullPointerException e) {
            System.out.println("Discounts not selected. " + e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showItemList();
        tv_items.setOnMouseClicked(mouseEvent -> {
            selectAddItemList();
            lbl_itemAmount.setText("0");
            lbl_itemTotal.setText("£0");
        });
        btn_plus1.setOnAction(event -> addOne());
        btn_minus1.setOnAction(event -> minusOne());
        btn_addToBasket.setOnAction(event -> {
            if (!lbl_itemTotal.getText().contains("£0")) {
                if (!basketMade) {
                    basketID = BasketTable.createBasket(customerEmail);
                    basketMade = true;
                }
                BasketItemTable.addItemToBasket(lbl_itemName.getText(), basketID, Integer.parseInt(lbl_itemAmount.getText()));
                ItemTable.updateItemAmount(lbl_itemName.getText(), Integer.parseInt(lbl_itemAmount.getText()));
                Repository.giveAlert("Item has been added to basket.", "confirmation");
                showItemList();
            } else {
                Repository.giveAlert("Can't add 0 to basket.", "error");
            }

        });
        btn_signout.setOnAction(event -> SceneHandler.changeScene(event, "Login.fxml", "Login", null, 600, 400));
        btn_browse.setOnAction(event -> {
            switchForm(event);
            showItemList();
        });
        btn_discounts.setOnAction(event -> {
            switchForm(event);
            showDiscountList();
        });
        btn_basket.setOnAction(event -> {
            switchForm(event);
            showBasketItemList();
            showBasketDiscountList();
            String normalTotalOrderCost = Double.toString(BasketItemTable.sumItems(basketID));
            lbl_basketTotalOrderCost.setText(normalTotalOrderCost);
            lbl_basketAdjustedOrderCost.setText(normalTotalOrderCost);
        });
        btn_viewPreviousBasket.setOnAction(event -> {//need to loop through new tableview and add those to basket and reshow order total
            tv_basketItem.getItems().clear();
            showOldBasketItemList();
            String normalTotalOrderCost = Double.toString(BasketItemTable.sumItems(oldBasketID));
            lbl_basketTotalOrderCost.setText(normalTotalOrderCost);
            lbl_basketAdjustedOrderCost.setText(normalTotalOrderCost);
        });
        tv_basketItem.setOnMouseClicked(mouseEvent -> {
            selectBasketItemList();
            lbl_basketItemTotalCost.setText(Double.toString(BasketItemTable.fetchTotalPriceForItems(lbl_basketItemName.getText(), basketID)));
        });
        tv_basketDiscounts.setOnMouseClicked(mouseEvent -> {
            selectBasketDiscountList();
            double currentOrderCost = Double.parseDouble(lbl_basketTotalOrderCost.getText());
            double percentageOff = Double.parseDouble(lbl_basketPercentageOff.getText());
            double adjustedOrderCost = currentOrderCost - (currentOrderCost * (percentageOff / 100));
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.DOWN);
            lbl_basketAdjustedOrderCost.setText(df.format(adjustedOrderCost));
        });
        btn_removeItemFromBasket.setOnAction(event -> deleteItemFromBasket(lbl_basketItemName.getText(), Integer.parseInt(lbl_basketItemQuantity.getText())));
        btn_purchase.setOnAction(event -> {
            if (Double.parseDouble(lbl_basketTotalOrderCost.getText()) > 0) {
                LocalTime localTime = LocalTime.now();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                Transaction transaction = new Transaction(customerEmail,
                        Double.parseDouble(lbl_basketAdjustedOrderCost.getText()),
                        LocalDate.now(),
                        localTime.format(timeFormatter));
                TransactionTable.addTransaction(transaction);//adds transaction to db
                BasketTable.makeBasketPurchased(basketID);//sets basket to purchased
                DiscountsTable.removeDiscount(customerEmail, Integer.parseInt(lbl_basketPercentageOff.getText()));
                DiscountsTable.giveUserDiscount(customerEmail);
                sendReceipt();
                Repository.giveAlert("Purchase has gone through.\nReceipt has been sent to your email address.", "confirmation");

                Stage stage = (Stage) btn_purchase.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("basket is empty.");
                Repository.giveAlert("Basket is empty.", "error");
            }
        });
    }


    private void switchForm(ActionEvent event) {
        if (event.getSource() == btn_browse) {
            browse_form.setVisible(true);
            discounts_form.setVisible(false);
            basket_form.setVisible(false);
            btn_browse.setStyle("-fx-background-color: #13a5ec;");
            btn_discounts.setStyle("-fx-background-color: transparent;");
            btn_basket.setStyle("-fx-background-color: transparent;");
        } else if (event.getSource() == btn_discounts) {
            browse_form.setVisible(false);
            discounts_form.setVisible(true);
            basket_form.setVisible(false);
            btn_browse.setStyle("-fx-background-color: transparent;");
            btn_discounts.setStyle("-fx-background-color: #13a5ec;");
            btn_basket.setStyle("-fx-background-color: transparent;");
        } else if (event.getSource() == btn_basket) {
            browse_form.setVisible(false);
            discounts_form.setVisible(false);
            basket_form.setVisible(true);
            btn_browse.setStyle("-fx-background-color: transparent;");
            btn_discounts.setStyle("-fx-background-color: transparent;");
            btn_basket.setStyle("-fx-background-color:#13a5ec; ");
        }
    }

    private void addOne() {
        try {
            int currentAmount = Integer.parseInt(lbl_itemAmount.getText());
            int quantityLeft = Integer.parseInt(lbl_itemQuantity.getText());
            if (quantityLeft > 0) {
                lbl_itemAmount.setText(Integer.toString(currentAmount + 1));
                lbl_itemQuantity.setText(Integer.toString(quantityLeft - 1));
                lbl_itemTotal.setText("£" + (currentAmount + 1) * Double.parseDouble(lbl_itemPrice.getText()));
            } else {
                Repository.giveAlert("No More of this item to add.", "error");
            }
        } catch (Exception e) {
            Repository.giveAlert("No item selected", "error");
        }
    }

    private void minusOne() {
        try {
            int currentAmount = Integer.parseInt(lbl_itemAmount.getText());
            int quantityLeft = Integer.parseInt(lbl_itemQuantity.getText());
            if (currentAmount > 0) {
                lbl_itemAmount.setText(Integer.toString(currentAmount - 1));
                lbl_itemQuantity.setText(Integer.toString(quantityLeft + 1));
                lbl_itemTotal.setText("£" + (currentAmount - 1) * Double.parseDouble(lbl_itemPrice.getText()));
            } else {
                Repository.giveAlert("You already have 0.", "error");
            }
        } catch (Exception e) {
            Repository.giveAlert("No item selected", "error");
        }
    }

    private void deleteItemFromBasket(String itemName, int quantityOfItem) {
        try {
            BasketItemTable.deleteRecord(basketID, itemName);
            ItemTable.updateItemAmount(itemName, -quantityOfItem);
            showBasketItemList();
            lbl_basketTotalOrderCost.setText(Double.toString(BasketItemTable.sumItems(basketID)));
        } catch (Exception e) {
            Repository.giveAlert("You haven't selected an item to remove.", "error");
        }
    }

    private void sendReceipt() {
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;
        ArrayList<BasketItem> basketItems = new ArrayList<>();
        StringBuilder text = new StringBuilder();
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT * FROM BasketItem WHERE BasketID = ?");
            prepare.setInt(1, basketID);
            resultSet = prepare.executeQuery();
            BasketItem basketItem;
            while (resultSet.next()) {
                basketItem = new BasketItem(resultSet.getString("ItemName"), resultSet.getInt("QuantityAdded"));
                basketItems.add(basketItem);
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
        for (BasketItem basketItem : basketItems) {
            checkForStockUp(basketItem.getItemName());//checks if this order makes any of the items low in stock
            text.append("\n Item Name: ").append(basketItem.getItemName()).append(" - ").append("Quantity: ").append(basketItem.getQuantityAdded());
        }
        text.append("\nTotal Order Cost: £").append(lbl_basketAdjustedOrderCost.getText());
        EmailToken emailToken = new EmailToken(customerEmail, null, "Receipt for Purchase", text.toString(), false);
        Email.sendEmail(emailToken);
    }

    private void checkForStockUp(String itemName) {//if an item is under a certain quantity it will be re-ordered, and email will be sent to admins
        int itemQuantity = Integer.parseInt(Utils.selectFromRecord("Quantity", "Items", "ItemName", itemName));
        if (itemQuantity < 50) {
            if (Utils.readAutoStockUpSetting()) {
                LocalTime localTime = LocalTime.now();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                double itemPriceForOne = Double.parseDouble(Utils.selectFromRecord("Price", "Items", "ItemName", itemName));
                ItemTable.updateItemAmount(itemName, -200);//method subtracts amount, so this increases it
                //assuming that buying an item in bulk would cost ~ 60% of selling price for reasonable profits
                Transaction transaction = new Transaction("aboubacre76@gmail.com", -(0.6 * (200 * (itemPriceForOne))), LocalDate.now(), localTime.format(timeFormatter));
                TransactionTable.addTransaction(transaction);
            } else {
                EmailToken emailToken = new EmailToken(customerEmail, "",
                        itemName + " item is low in stock.",
                        "This is an automated message.\n this item is low in stock and needs to be re-ordered.",
                        false);
                Email.sendEmail(emailToken);
            }
        }
    }

    public void closeCustomerWindow() {
        if (basketID > 0) {
            if (Utils.selectFromRecord("Purchased", "Basket", "BasketID", Integer.toString(basketID)).equalsIgnoreCase("FALSE")) {
                ArrayList<BasketItem> basketItems = BasketItemTable.createBasketItemList(basketID);
                for (BasketItem basketItem : basketItems) {
                    deleteItemFromBasket(basketItem.getItemName(), basketItem.getQuantityAdded());
                }
            }
        }
    }

    public void setCustomerEmail(String forwardedEmail) {
        customerEmail = forwardedEmail;
        System.out.println("Email: " + customerEmail);
    }
}
