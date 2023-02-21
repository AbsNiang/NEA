package com.example.demo.Controllers;

import com.example.demo.Database.*;
import com.example.demo.General.Repository;
import com.example.demo.Objects.Item;
import com.example.demo.Objects.Transaction;
import com.example.demo.Objects.User;
import com.example.demo.SceneHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.example.demo.Database.Utils.dbLocation;

public class AdminLoggedInController implements Initializable { //Scene once signed in (admin)

    //Side Controls
    @FXML
    private Button btn_signout;
    @FXML
    private Button btn_logistics;
    @FXML
    private Button btn_addItems;
    @FXML
    private Button btn_editUsers;
    @FXML
    private Button btn_customerView;

    //Logistics Anchor Pane:
    @FXML
    private AnchorPane logistics_form;
    @FXML
    private Label lbl_profits;
    @FXML
    private Label lbl_availableIndividualItems;
    @FXML
    private Label lbl_availableTotalItems;
    @FXML
    private CheckBox cb_autoOrderStock;
    @FXML
    private LineChart<CategoryAxis, NumberAxis> cashFlowLineChart;
    @FXML
    private BarChart<CategoryAxis, NumberAxis> itemOrdersBarChart;

    //Add Items Anchor Pane: //add items will count as a transaction for the business as it costs money to get stock irl
    @FXML
    private AnchorPane addItems_form;
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
    private TextField tf_itemBulkPrice;
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
    private TableColumn<?, ?> tvCol_editEmail;
    @FXML
    private TableColumn<?, ?> tvCol_editFirstName;
    @FXML
    private TableColumn<?, ?> tvCol_editSurname;
    @FXML
    private TableColumn<?, ?> tvCol_editIsAdmin;
    @FXML
    private TableView<User> tv_users;

    private User selectedUser;
    private Item selectedItem;
    private String adminEmailAddress;

    //Item TableView Stuff
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

    public void showAddItemList() {
        ObservableList<Item> listAddItem = addItemList();
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

    //Users TableView Stuff
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
                        resultSet.getBoolean("IsAdmin"));
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
        ObservableList<User> listEditUsers = editUsersList();
        tvCol_editEmail.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        tvCol_editFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tvCol_editSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tvCol_editIsAdmin.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));// error for debug stage
        tv_users.setItems(listEditUsers);
    }

    public void selectEditUsersList() {
        User user = tv_users.getSelectionModel().getSelectedItem();
        tf_email.setText(user.getEmailAddress());
        tf_firstName.setText(user.getFirstName());
        tf_surname.setText(user.getSurname());
        cb_isAdmin.setSelected(user.isAdmin());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLogistics();
        //Item TableView
        tv_addItems.setOnMouseClicked(mouseEvent -> {
            try {
                selectAddItemList();
                selectedItem = new Item(tf_itemName.getText(), Double.parseDouble(tf_itemPrice.getText()), Integer.parseInt(tf_itemQuantity.getText()), ta_itemTags.getText(), ta_itemDescription.getText());
            } catch (Exception e) {
                System.out.println("item not selected.");
            }
        });
        //User TableView
        tv_users.setOnMouseClicked(mouseEvent -> {
            selectEditUsersList();
            tf_itemBulkPrice.setText("");
            selectedUser = new User(tf_email.getText(), null, null, tf_firstName.getText(), tf_surname.getText(), cb_isAdmin.isSelected());
        });
        btn_submitUserChanges.setOnAction(event -> {
            try {
                Utils.updateInfo(selectedUser.getEmailAddress(), "EmailAddress", tf_email.getText(), "USERS", "EmailAddress");
                Utils.updateInfo(selectedUser.getEmailAddress(), "FirstName", tf_firstName.getText(), "USERS", "EmailAddress");
                Utils.updateInfo(selectedUser.getEmailAddress(), "Surname", tf_surname.getText(), "USERS", "EmailAddress");
                UserTable.updateBooleanInfo(selectedUser.getEmailAddress(), "IsAdmin", cb_isAdmin.isSelected());
                showEditUsersList();
            } catch (Exception e) {
                Repository.giveAlert("This isn't a pre-existing account.", "error");
            }

        });
        btn_deleteUser.setOnAction(event -> {
            try {
                UserTable.deleteUser(tf_email.getText());
                showEditUsersList();
            } catch (Exception e) {
                Repository.giveAlert("Can't do that!", "error");
            }
            showEditUsersList();
        });
        btn_signout.setOnAction(actionEvent -> SceneHandler.changeScene(actionEvent, "Login.fxml", "Login", adminEmailAddress, 600, 400));
        btn_logistics.setOnAction(this::switchForm);
        cb_autoOrderStock.setOnMouseClicked(mouseEvent -> Utils.changeAutoStockUpSetting(cb_autoOrderStock.isSelected()));
        btn_addItems.setOnAction(actionEvent -> {
            switchForm(actionEvent);
            showAddItemList();
        });
        btn_editUsers.setOnAction(actionEvent -> {
            switchForm(actionEvent);
            showEditUsersList();
        });
        btn_customerView.setOnAction(event -> SceneHandler.changeScene(event, "CustomerLoggedIn.fxml", "Welcome", adminEmailAddress, 1100, 651));
        btn_addItem.setOnAction(actionEvent -> {
            Item item = new Item(tf_itemName.getText(),
                    Double.parseDouble(tf_itemPrice.getText()),
                    Integer.parseInt(tf_itemQuantity.getText()),
                    ta_itemTags.getText(),
                    ta_itemDescription.getText());
            ItemTable.insertItem(item);
            LocalTime localTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            // will sum all transactions made by emails with admin set as true when calculating losses
            Transaction transaction = new Transaction(adminEmailAddress, -Double.parseDouble(tf_itemBulkPrice.getText()), LocalDate.now(), localTime.format(timeFormatter));
            TransactionTable.addTransaction(transaction);
            showAddItemList();
        });
        btn_updateItem.setOnAction(event -> {//name, price, quantity, tags, desc.
            if (!(selectedItem == null)) {
                String name = selectedItem.getName();
                Utils.updateInfo(name, "ItemName", tf_itemName.getText(), "ITEMS", "ItemName");
                Utils.updateInfo(name, "Price", tf_itemPrice.getText(), "ITEMS", "ItemName");
                int newItemQuantity = Integer.parseInt(tf_itemQuantity.getText());
                int oldItemQuantity = Integer.parseInt(Utils.selectFromRecord("Quantity", "Items", "ItemName", name));
                LocalTime localTime = LocalTime.now();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                // will sum all transactions made by emails with admin set as true when calculating losses, assuming bulk price is 60% of selling price
                if (newItemQuantity != oldItemQuantity) {
                    Transaction transaction = new Transaction(adminEmailAddress, -(0.6 * (newItemQuantity - oldItemQuantity)), LocalDate.now(), localTime.format(timeFormatter));
                    TransactionTable.addTransaction(transaction);
                }
                Utils.updateInfo(name, "Quantity", Integer.toString(newItemQuantity), "ITEMS", "ItemName");
                Utils.updateInfo(name, "Tags", ta_itemTags.getText(), "ITEMS", "ItemName");
                Utils.updateInfo(name, "Description", ta_itemDescription.getText(), "ITEMS", "ItemName");
                showAddItemList();
            } else {
                Repository.giveAlert("Item hasn't been selected", "error");
            }
        });
    }

    private void switchForm(ActionEvent event) {
        if (event.getSource() == btn_logistics) {
            logistics_form.setVisible(true);
            addItems_form.setVisible(false);
            editUsers_form.setVisible(false);
            btn_logistics.setStyle("-fx-background-color: #13a5ec;");
            btn_addItems.setStyle("-fx-background-color: transparent;");
            btn_editUsers.setStyle("-fx-background-color: transparent;");

        } else if (event.getSource() == btn_addItems) {
            logistics_form.setVisible(false);
            addItems_form.setVisible(true);
            editUsers_form.setVisible(false);
            btn_logistics.setStyle("-fx-background-color: transparent;");
            btn_addItems.setStyle("-fx-background-color: #13a5ec;");
            btn_editUsers.setStyle("-fx-background-color: transparent;");
        } else if (event.getSource() == btn_editUsers) {
            logistics_form.setVisible(false);
            addItems_form.setVisible(false);
            editUsers_form.setVisible(true);
            btn_logistics.setStyle("-fx-background-color: transparent;");
            btn_addItems.setStyle("-fx-background-color: transparent;");
            btn_editUsers.setStyle("-fx-background-color:#13a5ec; ");
        }
    }

    public void setAdminEmail(String forwardedEmail) {
        adminEmailAddress = forwardedEmail;
        System.out.println("Email: " + adminEmailAddress);
    }

    private void setupLogistics() {
        lbl_profits.setText("£" + TransactionTable.sumTransactions(adminEmailAddress, false));
        lbl_availableTotalItems.setText(Integer.toString(ItemTable.sumItems()));
        lbl_availableIndividualItems.setText(Integer.toString(ItemTable.countItems()));
        cb_autoOrderStock.setSelected(Utils.readAutoStockUpSetting());
        setUpCashFlowChart();
        setUpItemOrdersChart();
    }

    private void setUpCashFlowChart() {
        XYChart.Series<CategoryAxis, NumberAxis> series = new XYChart.Series<>();
        series.setName("Money");
        ArrayList<Transaction> transactions = TransactionTable.getTransactionsList();
        for (Transaction transaction : transactions) {
            series.getData().add(new XYChart.Data("" + transaction.getDateOfTransaction().getDayOfYear(), transaction.getMoneySpent()));
        }
        cashFlowLineChart.getData().add(series);
    }

    private void setUpItemOrdersChart() {
        XYChart.Series<CategoryAxis, NumberAxis> series = new XYChart.Series<>();
        series.setName("Total Ordered");
        HashMap<String, Integer> itemTotalOrderedList = BasketItemTable.getBasketItemTotalOrderedList();
        for (Map.Entry<String, Integer> entry : itemTotalOrderedList.entrySet()) {
            String itemName = entry.getKey();
            int totalOrderedAmount = entry.getValue();
            series.getData().add(new XYChart.Data(itemName, totalOrderedAmount));
        }
        itemOrdersBarChart.getData().add(series);
    }
}
