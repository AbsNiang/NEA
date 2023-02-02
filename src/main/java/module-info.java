module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires javafx.graphics;
    requires java.desktop;

    /*
    exports com.example.demo.auth;
    opens com.example.demo.auth to javafx.fxml;
    */

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.Registration;
    opens com.example.demo.Registration to javafx.fxml;
    exports com.example.demo.Controllers;
    opens com.example.demo.Controllers to javafx.fxml;
    exports com.example.demo.EmailHandling;
    opens com.example.demo.EmailHandling to javafx.fxml;
    exports com.example.demo.Database;
    opens com.example.demo.Database to javafx.fxml;
    exports com.example.demo.Objects;
    opens com.example.demo.Objects to javafx.fxml;

}