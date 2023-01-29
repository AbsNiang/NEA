package com.example.demo.auth.Controllers;

import com.example.demo.Database.BasketItemTable;
import com.example.demo.Database.BasketTable;
import com.example.demo.Database.ItemTable;
import com.example.demo.Database.Utils;
import com.example.demo.SceneHandler;
import com.example.demo.auth.Objects.BasketItem;
import com.example.demo.auth.Objects.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.example.demo.Database.Utils.dbLocation;

public class CustomerLoggedInController implements Initializable {


    //Side Controls
    @FXML
    private Button btn_browse;
    @FXML
    private Button btn_coupons;
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
    private TextField tf_searchItems;
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

    //Coupons Anchor Pane:
    @FXML
    private AnchorPane coupons_form;

    //Basket Anchor Pane:
    @FXML
    private AnchorPane basket_form;
    @FXML
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
    private Button btn_checkout;





    private boolean basketMade = false;
    private String customerEmail;
    private int basketID;

    //Item TableView Stuff
    public ObservableList<Item> addItemList() {
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


    public void showItemList() {
        ObservableList<Item> listAddItem = addItemList();
        tvCol_itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tvCol_itemPrice.setCellValueFactory(new PropertyValueFactory<>("cost"));
        tvCol_itemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tvCol_itemTags.setCellValueFactory(new PropertyValueFactory<>("tags"));
        tvCol_itemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tv_items.setItems(listAddItem);
    }

    public void selectAddItemList() {
        Item item = tv_items.getSelectionModel().getSelectedItem();
        lbl_itemName.setText(item.getName());
        lbl_itemPrice.setText(Double.toString(item.getCost()));
        lbl_itemQuantity.setText(Integer.toString(item.getQuantity()));
        lbl_itemDescription.setText(item.getDescription());
    }

    public ObservableList<BasketItem> basketItemList() {
        ObservableList<BasketItem> listData = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT * FROM BasketItem WHERE BasketID = ?");
            prepare.setInt(1,basketID);
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


    public void showBasketItemList() {
        ObservableList<BasketItem> listAddItem = basketItemList();
        tvCol_basketItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tvCol_basketItemQuantityAdded.setCellValueFactory(new PropertyValueFactory<>("quantityAdded"));
        tv_basketItem.setItems(listAddItem);
    }

    public void selectBasketItemList() {
        BasketItem basketItem = tv_basketItem.getSelectionModel().getSelectedItem();
        lbl_basketItemName.setText(basketItem.getItemName());
        lbl_basketItemQuantity.setText(Integer.toString(basketItem.getQuantityAdded()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showItemList();
        tv_items.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectAddItemList();
                lbl_itemAmount.setText("0");
            }
        });
        btn_plus1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addOne();
            }
        });
        btn_minus1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                minusOne();
            }
        });
        btn_addToBasket.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!basketMade) {
                    basketID = BasketTable.createBasket(customerEmail);
                    basketMade = true;
                }
                BasketItemTable.addItemToBasket(lbl_itemName.getText(), basketID, Integer.parseInt(lbl_itemAmount.getText()));
                ItemTable.updateItemAmount(lbl_itemName.getText(), Integer.parseInt(lbl_itemAmount.getText()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Item has been added to basket.");
                alert.show();
                showItemList();
            }
        });
        btn_signout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneHandler.changeScene(event, "Login.fxml", "Login", null, 600, 400);
            }
        });
        btn_browse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchForm(event);
            }
        });
        btn_coupons.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchForm(event);
            }
        });
        btn_basket.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchForm(event);
                showBasketItemList();
            }
        });
        tv_basketItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectBasketItemList();
            }
        });
    }


    public void switchForm(ActionEvent event) {
        if (event.getSource() == btn_browse) {
            browse_form.setVisible(true);
            coupons_form.setVisible(false);
            basket_form.setVisible(false);
            btn_browse.setStyle("-fx-background-color: #13a5ec;");
            btn_coupons.setStyle("-fx-background-color: transparent;");
            btn_basket.setStyle("-fx-background-color: transparent;");
        } else if (event.getSource() == btn_coupons) {
            browse_form.setVisible(false);
            coupons_form.setVisible(true);
            basket_form.setVisible(false);
            btn_browse.setStyle("-fx-background-color: transparent;");
            btn_coupons.setStyle("-fx-background-color: #13a5ec;");
            btn_basket.setStyle("-fx-background-color: transparent;");
        } else if (event.getSource() == btn_basket) {
            browse_form.setVisible(false);
            coupons_form.setVisible(false);
            basket_form.setVisible(true);
            btn_browse.setStyle("-fx-background-color: transparent;");
            btn_coupons.setStyle("-fx-background-color: transparent;");
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
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No More of this item to add.");
                alert.show();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No item selected");
            alert.show();
        }
    }

    private void minusOne() {
        try {
            int currentAmount = Integer.parseInt(lbl_itemAmount.getText());
            int quantityLeft = Integer.parseInt(lbl_itemQuantity.getText());
            if (currentAmount > 0) {
                lbl_itemAmount.setText(Integer.toString(currentAmount - 1));
                lbl_itemQuantity.setText(Integer.toString(quantityLeft + 1));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You already have 0.");
                alert.show();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No item selected");
            alert.show();
        }
    }

    public void setCustomerEmail(String forwardedEmail) {
        customerEmail = forwardedEmail;
        System.out.println(customerEmail);
    }
}
