module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    /*
    exports com.example.demo.auth;
    opens com.example.demo.auth to javafx.fxml;
    */
    exports com.example.demo.auth.Registration;
    opens com.example.demo.auth.Registration to javafx.fxml;
    exports com.example.demo.auth.Controllers;
    opens com.example.demo.auth.Controllers to javafx.fxml;
}