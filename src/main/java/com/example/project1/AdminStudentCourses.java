package com.example.project1;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AdminStudentCourses extends Application {

    private Stage primaryStage;
    private ComboBox<Integer> levelComboBox;
    private ComboBox<Integer> studentComboBox;
    private ComboBox<String> courseComboBox;
    private DatePicker examDatePicker;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        StackPane stackPane = createStackPane();

        // Set up stage
        primaryStage.setTitle("Student Course Registration");
        primaryStage.setScene(new Scene(stackPane, 1000, 800));
        setupFullScreen(primaryStage);
        primaryStage.show();
    }

    private StackPane createStackPane() {
        StackPane stackPane = new StackPane();
        ImageView backgroundView = createBackgroundImageView();
        stackPane.getChildren().add(backgroundView);

        GridPane gridPane = createGridPane();
        stackPane.getChildren().add(gridPane);

        return stackPane;
    }

    private ImageView createBackgroundImageView() {
        Image backgroundImage = new Image(getClass().getResource("/university-background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000);
        backgroundView.setFitHeight(1000);
        return backgroundView;
    }

    private void setupFullScreen(Stage primaryStage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);

        addLevelComboBoxToGrid(gridPane);
        addStudentComboBoxToGrid(gridPane);
        addCourseComboBoxToGrid(gridPane);
        addExamDatePickerToGrid(gridPane);
        addButtonsToGrid(gridPane);

        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private void addLevelComboBoxToGrid(GridPane gridPane) {
        Label levelLabel = createLabel("Level:");
        levelComboBox = new ComboBox<>();
        levelComboBox.getItems().addAll(1, 2, 3, 4);
        levelComboBox.setStyle("-fx-font-size: 18;");
        levelComboBox.setOnAction(e -> populateStudentComboBox(levelComboBox.getValue()));
        gridPane.addRow(0, levelLabel, levelComboBox);
    }

    private void addStudentComboBoxToGrid(GridPane gridPane) {
        Label studentIdLabel = createLabel("Student ID:");
        studentComboBox = new ComboBox<>();
        studentComboBox.setStyle("-fx-font-size: 18;");
        studentComboBox.setOnAction(e -> onStudentSelected());
        gridPane.addRow(1, studentIdLabel, studentComboBox);
    }

    private void addCourseComboBoxToGrid(GridPane gridPane) {
        Label courseNameLabel = createLabel("Course Name:");
        courseComboBox = new ComboBox<>();
        courseComboBox.setStyle("-fx-font-size: 18;");
        gridPane.addRow(2, courseNameLabel, courseComboBox);
    }

    private void addExamDatePickerToGrid(GridPane gridPane) {
        Label examDateLabel = createLabel("Exam Date:");
        examDatePicker = new DatePicker();
        examDatePicker.setStyle("-fx-font-size: 18;");
        gridPane.addRow(3, examDateLabel, examDatePicker);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        return label;
    }

    private void addButtonsToGrid(GridPane gridPane) {
        Button registerButton = createButton("Register Course", e -> registerCourse());
        Button backButton = createButton("Back", e -> Utils.goToAdminDashBoardPage(primaryStage));

        HBox buttonBox = new HBox(10, registerButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        gridPane.add(buttonBox, 0, 4, 2, 1);
    }

    private Button createButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        button.setStyle("-fx-background-color: #505050; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;");
        return button;
    }

    private void populateStudentComboBox(Integer level) {
        studentComboBox.getItems().clear();
        String query = "SELECT studentId FROM students WHERE level = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, level);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                studentComboBox.getItems().add(resultSet.getInt("studentId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void onStudentSelected() {
        Integer studentId = studentComboBox.getValue();
        if (studentId != null) {
            populateCourseComboBox(studentId);
        }
    }

    private void populateCourseComboBox(Integer studentId) {
        courseComboBox.getItems().clear();
        String query = """
                SELECT c.courseName
                FROM courses c
                JOIN students s ON c.courseDepartment = s.departmentId
                WHERE s.studentId = ?
                """;
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseComboBox.getItems().add(resultSet.getString("courseName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerCourse() {
        Integer studentId = studentComboBox.getValue();
        String courseName = courseComboBox.getValue();
        LocalDate examDate = examDatePicker.getValue();

        if (studentId == null || courseName == null || examDate == null) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
            return;
        }

        int courseId = getCourseIdByName(courseName);
        String insertQuery = "INSERT INTO studentcourses (studentId, courseId, examDate) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertQuery)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.setDate(3, java.sql.Date.valueOf(examDate));
            statement.executeUpdate();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Course registered successfully!");
        } catch (SQLException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    // Unregister course for student
    private void unregisterCourse() {
        Integer studentId = studentComboBox.getValue();
        String courseName = courseComboBox.getValue();

        if (studentId == null || courseName == null) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
            return;
        }

        int courseId = getCourseIdByName(courseName);
        String deleteQuery = "DELETE FROM studentcourses WHERE studentId = ? AND courseId = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteQuery)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Course unregistered successfully!");
        } catch (SQLException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    // Get course ID by course name
    private int getCourseIdByName(String courseName) {
        String query = "SELECT courseId FROM courses WHERE courseName = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("courseId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if course does not exist
    }

    public static void main(String[] args) {
        launch();
    }
}

