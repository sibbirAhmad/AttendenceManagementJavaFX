module com.example.attendencemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.attendencemanagement to javafx.fxml;
    exports com.example.attendencemanagement;
}