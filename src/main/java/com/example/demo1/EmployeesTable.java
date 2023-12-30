package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmployeesTable extends Application {
    private Stage primaryStage;
    static TableView<Employee> employeeTableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage=primaryStage;
        final int maxRows = 6;

        employeeTableView.setRowFactory(tv -> new TableRow<Employee>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    getStyleClass().add("table-row-hidden");
                } else {
                    getStyleClass().remove("table-row-hidden");
                }
            }
        });
        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        nameCol.setPrefWidth(520);
        TableColumn<Employee, Integer> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(data -> data.getValue().codeProperty().asObject());
        codeCol.setPrefWidth(520);
        TableColumn<Employee, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(data -> data.getValue().departmentProperty());
        deptCol.setPrefWidth(510);
        employeeTableView.getColumns().addAll(nameCol, codeCol, deptCol);
        StackPane stackPane=new StackPane();
        Button button1=new Button("Back");
        button1.setPrefWidth(200); // Set preferred width
        button1.setPrefHeight(60);
        button1.setStyle("-fx-background-color: grey; " + // Aqua color
                "-fx-text-fill: white; " + // Text color
                "-fx-border-radius: 10; " + // Border radius
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;");
        button1.setOnAction(e ->{
            if(HelloApplication.myrole.equals("admin")) {
                Admin admin = new Admin();
                try {
                    admin.start(new Stage());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                EmpAdmin empAdmin=new EmpAdmin();
                try {
                    empAdmin.start(new Stage());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            primaryStage.close();
        });

        stackPane.getChildren().add(employeeTableView);
        stackPane.getChildren().add(button1);


        employeeTableView.getStyleClass().add("table-background");
        employeeTableView.getStyleClass().add("table-cell-centered");
        refreshTable(); // Fetch data from the database

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        Scene scene = new Scene(stackPane, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Information");
        primaryStage.show();
    }
    String url = "jdbc:mysql://localhost:3306/studentsystem";
    String user = "root";
    String password = "";
    private void fetchCourseData() {
        // Database connection information


        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement statement = conn.createStatement()) {

            // Assuming 'courses' table structure: name, code, department
            String query = "SELECT name, code, department FROM employees";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int code = resultSet.getInt("code");
                String department = resultSet.getString("department");

                Employee employee = new Employee(name, code, department);
                employeeTableView.getItems().add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshTable() {
        employeeTableView.getItems().clear(); // Clear the existing items in the TableView
        fetchCourseData(); // Fetch and display updated data in the table
    }
    public static void main(String[] args) {
        launch(args);
    }
}
