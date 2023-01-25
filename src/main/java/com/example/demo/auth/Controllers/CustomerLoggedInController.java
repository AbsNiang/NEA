package com.example.demo.auth.Controllers;

import com.example.demo.auth.Objects.Item;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerLoggedInController implements Initializable {

    //Side Controls
    private Button btn_browse;
    private Button btn_coupons;
    private Button btn_basket;
    private Button btn_signout;

    //Browse Anchor Pane:
    private Button btn_plus1;
    private Button btn_minus1;
    private Button btn_addToBasket;
    private Label lbl_itemName;
    private Label lbl_itemPrice;
    private Label lbl_itemQuantity;
    private Label lbl_itemAmount;
    private Label lbl_itemTotal;
    private TableView<Item> tv_items;
    private TextField tf_searchItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
