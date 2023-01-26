package com.example.demo.auth.Controllers;

import com.example.demo.SceneHandler;
import com.example.demo.auth.Objects.Item;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

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
    private TableView<Item> tv_items;
    @FXML
    private TextField tf_searchItems;
    @FXML
    private TableColumn<?, ?> tvCol_itemName;
    @FXML
    private TableColumn<?, ?> tvCol_itemPrice;
    @FXML
    private TableColumn<?, ?> tvCol_itemQuantity;
    @FXML
    private TableColumn<?, ?> tvCol_itemTags;
    @FXML
    private TableColumn<?, ?> tvCol_itemDescription;

    @FXML
    private AnchorPane coupons_form;

    @FXML
    private AnchorPane basket_form;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}
