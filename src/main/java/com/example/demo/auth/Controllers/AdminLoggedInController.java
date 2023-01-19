package com.example.demo.auth.Controllers;

import com.example.demo.SceneHandler;
import com.example.demo.auth.Objects.Item;
import com.example.demo.auth.Objects.User;
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

import static com.example.demo.DBUtils.UserTable.dbLocation;

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
    private Button btn_customerView;
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
    private AnchorPane itemNotFound_form; // turns visible if the item isn't found in the grocery item price dataset
    @FXML
    private TextField tf_searchAddItems;
    @FXML
    private TableColumn<Item, String> tvCol_addItemName;
    @FXML
    private TableColumn<Item, String> tvCol_addItemPrice;
    @FXML
    private TableColumn<Item, String> tvCol_addItemQuantity;
    @FXML
    private TableColumn<Item, String> tvCol_addItemTags;
    @FXML
    private TableColumn<Item, String> tvCol_addItemDescription;
    @FXML
    private TableView<Item> tv_addItems;

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
    private TableColumn<?, ?> tvCol_editItemQuantity;
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
    private TextField tf_email;
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
    private TableColumn<?, ?> tvCol_editEmail;
    @FXML
    private TableColumn<?, ?> tvCol_editFirstName;
    @FXML
    private TableColumn<?, ?> tvCol_editSurname;
    @FXML
    private TableColumn<?, ?> tvCol_editIsAdmin;
    @FXML
    private TableColumn<?, ?> tvCol_editHasLoyaltyCard;
    @FXML
    private TableView<User> tv_users;

    private ObservableList<Item> listAddItem;
    private ObservableList<User>listEditUsers;

    public ObservableList<Item> addItemList() {
        ObservableList<Item> listData = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT * FROM items");
            resultSet = prepare.executeQuery();
            Item item;
            while (resultSet.next()) {
                item = new Item(resultSet.getString("Name"),
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


    public void showAddItemList() {
        listAddItem = addItemList();
        tvCol_addItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tvCol_addItemPrice.setCellValueFactory(new PropertyValueFactory<>("cost"));
        tvCol_addItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tvCol_addItemTags.setCellValueFactory(new PropertyValueFactory<>("tags"));
        tvCol_addItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tv_addItems.setItems(listAddItem);
    }

    public void selectAddItemList() {
        Item item = tv_addItems.getSelectionModel().getSelectedItem();
        tf_itemName.setText(item.getName());
        tf_itemPrice.setText(Double.toString(item.getCost()));
        tf_itemQuantity.setText(Integer.toString(item.getQuantity()));
        ta_itemTags.setText(item.getTags());
        ta_itemDescription.setText(item.getDescription());
    }

    public ObservableList<User> editUsersList() {
        ObservableList<User> listData = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement prepare = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbLocation, "", "");
            prepare = connection.prepareStatement("SELECT * FROM users");
            resultSet = prepare.executeQuery();
            User user;
            while (resultSet.next()) {
                user = new User(resultSet.getString("EmailAddress"),
                        resultSet.getString("Password"),
                        resultSet.getString("PasswordSalt"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("Surname"),
                        resultSet.getBoolean("HasLoyaltyCard"),
                        resultSet.getBoolean("IsOwner"));
                listData.add(user);
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


    public void showEditUsersList() {
        listEditUsers = editUsersList();
        tvCol_editEmail.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        tvCol_editFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tvCol_editSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tvCol_editIsAdmin.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));
        tvCol_editHasLoyaltyCard.setCellValueFactory(new PropertyValueFactory<>("hasLoyaltyCard"));
        tv_users.setItems(listEditUsers);
    }

    public void selectEditUsersList() {
        User user = tv_users.getSelectionModel().getSelectedItem();
        tf_email.setText(user.getEmailAddress());
        tf_firstName.setText(user.getFirstName());
        cb_isAdmin.setSelected(user.isAdmin());
        cb_hasLoyaltyCard.setSelected(user.isHasLoyaltyCard());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAddItemList();
        showEditUsersList();
        tv_addItems.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectAddItemList();
            }
        });
        tv_users.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectEditUsersList();
            }
        });
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

        btn_customerView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneHandler.changeScene(event, "CustomerLoggedIn.fxml","Welcome","",1100,651);
            }
        });

        btn_addItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Item item = new Item(tf_itemName.getText(),
                        Double.parseDouble(tf_itemPrice.getText()),
                        Integer.parseInt(tf_itemQuantity.getText()),
                        ta_itemTags.getText(),
                        ta_itemDescription.getText());
            }
        });
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == btn_logistics) {
            logistics_form.setVisible(true);
            addItems_form.setVisible(false);
            editUsers_form.setVisible(false);
            editItems_form.setVisible(false);
            btn_logistics.setStyle("-fx-background-color: #13a5ec;");
            btn_addItems.setStyle("-fx-background-color: transparent;");
            btn_editUsers.setStyle("-fx-background-color: transparent;");
            btn_editItems.setStyle("-fx-background-color: transparent;");
        } else if (event.getSource() == btn_addItems) {
            logistics_form.setVisible(false);
            addItems_form.setVisible(true);
            editUsers_form.setVisible(false);
            editItems_form.setVisible(false);
            btn_logistics.setStyle("-fx-background-color: transparent;");
            btn_addItems.setStyle("-fx-background-color: #13a5ec;");
            btn_editUsers.setStyle("-fx-background-color: transparent;");
            btn_editItems.setStyle("-fx-background-color: transparent;");
        } else if (event.getSource() == btn_editUsers) {
            logistics_form.setVisible(false);
            addItems_form.setVisible(false);
            editUsers_form.setVisible(true);
            editItems_form.setVisible(false);
            btn_logistics.setStyle("-fx-background-color: transparent;");
            btn_addItems.setStyle("-fx-background-color: transparent;");
            btn_editUsers.setStyle("-fx-background-color:#13a5ec; ");
            btn_editItems.setStyle("-fx-background-color: transparent;");
        } else if (event.getSource() == btn_editItems) {
            logistics_form.setVisible(false);
            addItems_form.setVisible(false);
            editUsers_form.setVisible(false);
            editItems_form.setVisible(true);
            btn_logistics.setStyle("-fx-background-color:transparent;");
            btn_addItems.setStyle("-fx-background-color: transparent;");
            btn_editUsers.setStyle("-fx-background-color: transparent;");
            btn_editItems.setStyle("-fx-background-color: #13a5ec;");
        }
    }
    public void updateItem() {
        Item item = new Item(tf_itemName.getText(),
                Double.parseDouble(tf_itemPrice.getText()),
                Integer.parseInt(tf_itemQuantity.getText()),
                ta_itemTags.getText(),
                ta_itemDescription.getText());
    }
}
