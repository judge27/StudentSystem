package com.example.project1;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopStudentsByGPA extends Application {

    private TableView<StudentModel> level1Table;
    private TableView<StudentModel> level2Table;
    private TableView<StudentModel> level3Table;
    private TableView<StudentModel> level4Table;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize tables for each level
        level1Table = createTable();
        level2Table = createTable();
        level3Table = createTable();
        level4Table = createTable();

        // Query the database and populate tables for each level
        loadTopStudentsForLevel(1, level1Table);
        loadTopStudentsForLevel(2, level2Table);
        loadTopStudentsForLevel(3, level3Table);
        loadTopStudentsForLevel(4, level4Table);

        // Create "Back" button
        Button backButton = new Button("Back to Dashboard");
        backButton.setPrefWidth(320); // Preferred button width
        backButton.setPrefHeight(60); // Preferred button height
        backButton.setStyle("-fx-background-color: #505050; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;");
        backButton.setOnAction(event -> Utils.goToAdminDashBoardPage(primaryStage));

        // Layout setup
        VBox vbox = new VBox(20,  level1Table, level2Table, level3Table, level4Table,backButton);  // 20px spacing between tables
        vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);  // This centers the button at the top
        vbox.setPadding(new Insets(20, 0, 20, 0));  // Set padding (top, right, bottom, left)

        // StackPane for full-screen layout
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(vbox);

        // Set full-screen and maximize
        Screen primaryScreen = Screen.getPrimary();
        stackPane.setMaxWidth(primaryScreen.getVisualBounds().getWidth());
        stackPane.setMaxHeight(primaryScreen.getVisualBounds().getHeight());

        // Create the scene and set it on the stage
        Scene scene = new Scene(stackPane);

        // Apply CSS stylesheet to the scene
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Full screen setup
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Top Students By GPA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // Create a generic table for student data (only ID, Name, and GPA)
    private TableView<StudentModel> createTable() {
        TableView<StudentModel> tableView = new TableView<>();

        // Student ID column
        TableColumn<StudentModel, Integer> idColumn = new TableColumn<>("Student ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty().asObject());
        idColumn.setPrefWidth(511);

        // Student Name column
        TableColumn<StudentModel, String> nameColumn = new TableColumn<>("Student Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        nameColumn.setPrefWidth(511);
        // GPA column
        TableColumn<StudentModel, Float> gpaColumn = new TableColumn<>("GPA");
        gpaColumn.setCellValueFactory(cellData -> cellData.getValue().studentGpaProperty().asObject());
        gpaColumn.setPrefWidth(511);
        // Add columns to the table
        tableView.getColumns().addAll(idColumn, nameColumn, gpaColumn);

        // Customize rows (Hiding empty rows)
        tableView.setRowFactory(tv -> new TableRow<StudentModel>() {
            @Override
            protected void updateItem(StudentModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    getStyleClass().add("table-row-hidden");
                } else {
                    getStyleClass().remove("table-row-hidden");
                }
            }
        });

        // Apply the table CSS classes here
        tableView.getStyleClass().add("table-background");
        tableView.getStyleClass().add("table-cell-centered");

        return tableView;
    }

    // Fetch and load the top 10 students for a given level from the database
    private void loadTopStudentsForLevel(int level, TableView<StudentModel> table) {
        List<StudentModel> students = fetchTopStudentsByLevel(level);
        table.getItems().setAll(students);
    }

    // Query database to get the top 10 students based on GPA for a given level
    private List<StudentModel> fetchTopStudentsByLevel(int level) {
        List<StudentModel> students = new ArrayList<>();

        String query = "SELECT studentId, studentName, gpa FROM students WHERE level = ? ORDER BY gpa DESC LIMIT 10";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, level);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int studentId = resultSet.getInt("studentId");
                String studentName = resultSet.getString("studentName");
                float studentGpa = resultSet.getFloat("gpa");

                // Add student to list
                students.add(new StudentModel(studentId, studentName, level, studentGpa));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
