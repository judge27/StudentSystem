package com.example.project1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentCoursePage extends Application {

    private final ObservableList<CourseModel> courses = FXCollections.observableArrayList();
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        final int maxRows = 6;
        this.primaryStage = primaryStage;

        // Create and set up the table
        TableView<CourseModel> table = createCourseTable();
        fetchDataFromDatabase();
        table.setItems(courses);

        // Create and set up the back button
        Button backButton = createStyledButton("Back To Dashboard");
        setBackButtonAction(backButton);

        // Stack pane layout
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(table, backButton);

        // Maximize window size to the screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        // Scene setup
        Scene scene = new Scene(stackPane, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        table.getStyleClass().add("table-background");
        table.getStyleClass().add("table-cell-centered");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Course Table from Database");
        primaryStage.setMaximized(true); // Maximize on start
        primaryStage.setResizable(true); // Allow resizing

        primaryStage.show();
    }

    // Create the course table with columns
    private TableView<CourseModel> createCourseTable() {
        TableView<CourseModel> table = new TableView<>();
        table.setRowFactory(tv -> new TableRow<CourseModel>() {
            @Override
            protected void updateItem(CourseModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    getStyleClass().add("table-row-hidden");
                } else {
                    getStyleClass().remove("table-row-hidden");
                }
            }
        });

        TableColumn<CourseModel, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());
        courseNameColumn.setPrefWidth(520);

        TableColumn<CourseModel, String> courseDepartmentColumn = new TableColumn<>("Department");
        courseDepartmentColumn.setCellValueFactory(cellData -> cellData.getValue().courseDepartmentProperty());
        courseDepartmentColumn.setPrefWidth(520);

        TableColumn<CourseModel, String> examDateColumn = new TableColumn<>("Exam Date");
        examDateColumn.setCellValueFactory(cellData -> cellData.getValue().examDateProperty());
        examDateColumn.setPrefWidth(520);

        table.getColumns().addAll(courseNameColumn, courseDepartmentColumn, examDateColumn);
        return table;
    }

    // Create a styled button
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(320); // Preferred button width
        button.setPrefHeight(60); // Preferred button height
        button.setStyle("-fx-background-color: #505050; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;");
        return button;
    }

    // Set the back button action
    private void setBackButtonAction(Button backButton) {
        backButton.setOnAction(e -> Utils.goToStudentDashBoardPage(primaryStage));
    }

    // Fetch data from the database and populate the courses list
    private void fetchDataFromDatabase() {
        String query = """
                SELECT c.courseId, c.courseName AS courseName, d.departmentName, sc.examDate
                FROM studentcourses sc
                JOIN courses c ON sc.courseId = c.courseId
                JOIN departments d ON c.courseDepartment = d.departmentId
                WHERE sc.studentId = ?
                """;

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, LoginPage.currentusername);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("courseId");
                String courseName = resultSet.getString("courseName");
                String courseDepartment = resultSet.getString("departmentName");
                String examDate = resultSet.getString("examDate");

                courses.add(new CourseModel(id, courseName, courseDepartment, examDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
