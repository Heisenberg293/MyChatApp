module com.example.mychatapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mychatapp to javafx.fxml;
    exports com.example.mychatapp;
}