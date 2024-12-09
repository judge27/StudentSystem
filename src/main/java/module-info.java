module com.example.project1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires spark.core;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.net.http;

    opens com.example.project1 to javafx.fxml;
    exports com.example.project1;
}