package com.example.demo.auth.Controllers;

import com.example.demo.SceneHandler;
import com.example.demo.auth.Objects.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLoggedInController implements Initializable { //Scene once signed in (admin)

    //Side Controls
    @FXML
    private Button btn_signout;
    @FXML
    private Button btn_logistics;
    @FXML
    private Button btn_addItems;
    @FXML
    private Button btn_editItems;
    @FXML
    private Button btn_editUsers;
    @FXML
    private Label lbl_username;

    //Logistics Anchor Pane:
    @FXML
    private AnchorPane logistics_form;
    @FXML
    private Label lbl_profits;
    @FXML
    private Label lbl_availableItems;

    //Add Items Anchor Pane:
    @FXML
    private AnchorPane addItems_form;
    @FXML
    private Button btn_importItem;
    @FXML
    private Button btn_updateItem;
    @FXML
    private Button btn_addItem;
    @FXML
    private Button btn_clearItem;
    @FXML
    private TextField tf_itemName;
    @FXML
    private TextField tf_itemPrice;
    @FXML
    private TextField tf_itemQuantity;
    @FXML
    private TextArea ta_itemTags;
    @FXML
    private TextArea ta_itemDescription;
    @FXML
    private TextField tf_searchAddItems;
    @FXML
    private TableColumn<?, ?> tvCol_addItemName;
    @FXML
    private TableColumn<?, ?> tvCol_addItemPrice;
    @FXML
    private TableColumn<?, ?> tvCol_addItemTags;
    @FXML
    private TableColumn<?, ?> tvCol_addItemDescription;
    @FXML
    private TableView<?> tv_addItems;

    //Edit Items Anchor Pane:
    @FXML
    private AnchorPane editItems_form;
    @FXML
    private Button btn_submitItemChanges;
    @FXML
    private Button btn_deleteItem;
    @FXML
    private Button btn_importEditItem;
    @FXML
    private TextField tf_editItemName;
    @FXML
    private TextField tf_editItemPrice;
    @FXML
    private TextArea ta_editItemTags;
    @FXML
    private TextArea ta_editItemDescription;
    @FXML
    private TableColumn<?, ?> tvCol_editItemName;
    @FXML
    private TableColumn<?, ?> tvCol_editItemPrice;
    @FXML
    private TableColumn<?, ?> tvCol_editItemTags;
    @FXML
    private TableColumn<?, ?> tvCol_editItemDescription;
    @FXML
    private TableView<?> tv_editItems;

    //Edit Users Anchor Pane:
    @FXML
    private AnchorPane editUsers_form;
    @FXML
    private Button btn_submitUserChanges;
    @FXML
    private Button btn_deleteUser;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_firstName;
    @FXML
    private TextField tf_surname;
    @FXML
    private CheckBox cb_isAdmin;
    @FXML
    private CheckBox cb_hasLoyaltyCard;
    @FXML
    private TextField tf_searchUser;
    @FXML
    private TableColumn<?, ?> tvCol_editUsername;
    @FXML
    private TableColumn<?, ?> tvCol_editFirstName;
    @FXML
    private TableColumn<?, ?> tvCol_editSurname;
    @FXML
    private TableColumn<?, ?> tvCol_editIsAdmin;
    @FXML
    private TableColumn<?, ?> tvCol_editHasLoyaltyCard;
    @FXML
    private TableView<?> tv_users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_signout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneHandler.changeScene(actionEvent, "Login.fxml", "Login", null, 600, 400);
            }
        });

        btn_logistics.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchForm(event);
            }
        });

        btn_addItems.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchForm(event);
            }
        });

        btn_editUsers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchForm(event);
            }
        });

        btn_editItems.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchForm(event);
            }
        });


    }

    public void switchForm(ActionEvent event){
        if (event.getSource() == btn_logistics){
            logistics_form.setVisible(true);
            addItems_form.setVisible(false);
            editUsers_form.setVisible(false);
            editItems_form.setVisible(false);
        }else if (event.getSource() == btn_addItems){
            logistics_form.setVisible(false);
            addItems_form.setVisible(true);
            editUsers_form.setVisible(false);
            editItems_form.setVisible(false);
        }else if (event.getSource() == btn_editUsers){
            logistics_form.setVisible(false);
            addItems_form.setVisible(false);
            editUsers_form.setVisible(true);
            editItems_form.setVisible(false);
        }else if (event.getSource() == btn_editItems){
            logistics_form.setVisible(false);
            addItems_form.setVisible(false);
            editUsers_form.setVisible(false);
            editItems_form.setVisible(true);
        }
    }

}
