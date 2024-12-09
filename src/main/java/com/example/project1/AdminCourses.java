package com.example.project1;

import javafx.application.Application;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminCourses extends Application {

    private Stage primaryStage;
    private TextField tname, tcode;
    private ComboBox<String> deptComboBox;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Set background image
        StackPane stackPane = createBackgroundImage();

        // Set up primary stage properties
        primaryStage.setTitle("Course Management");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        setupStageBounds(primaryStage);

        // Create and configure form layout
        GridPane gridPane = createFormLayout();
        stackPane.getChildren().add(gridPane);

        // Set scene and stage
        Scene scene = new Scene(stackPane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createBackgroundImage() {
        StackPane stackPane = new StackPane();
        Image backgroundImage = new Image("file:src/main/resources/university-background.jpg");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000);
        backgroundView.setFitHeight(1000);
        stackPane.getChildren().add(backgroundView);
        return stackPane;
    }

    private void setupStageBounds(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
    }

    private GridPane createFormLayout() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Create form fields using the helper methods
        tname = createTextFieldWithLabel("Course Name:", gridPane, 0);
        tcode = createTextFieldWithLabel("Course Code:", gridPane, 1);
        deptComboBox = createComboBoxWithLabel("Department Name:", gridPane, 2);

        // Populate ComboBoxes
        populateDepartmentComboBox();

        // Add buttons
        HBox buttonBox = createButtonBox();
        gridPane.add(buttonBox, 0, 4);

        return gridPane;
    }

    private TextField createTextFieldWithLabel(String labelText, GridPane gridPane, int rowIndex) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        TextField textField = new TextField();
        HBox hBox = new HBox(10, label, textField);
        hBox.setPadding(new Insets(30, 10, 10, 120));
        gridPane.add(hBox, 0, rowIndex);
        return textField;
    }

    private ComboBox<String> createComboBoxWithLabel(String labelText, GridPane gridPane, int rowIndex) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-font-size: 18;");
        HBox hBox = new HBox(10, label, comboBox);
        hBox.setPadding(new Insets(30, 10, 10, 120));
        gridPane.add(hBox, 0, rowIndex);
        return comboBox;
    }

    private HBox createButtonBox() {
        // Create buttons using the helper method
        Button addButton = createStyledButton("Add Course", event -> insertData());
        Button updateButton = createStyledButton("Update Course", event -> updateData());
        Button deleteButton = createStyledButton("Delete Course", event -> deleteData());
        Button backButton = createStyledButton("Back", event -> Utils.goToAdminDashBoardPage(primaryStage));

        HBox hBox = new HBox(31, addButton, updateButton, deleteButton, backButton);
        hBox.setPadding(new Insets(30, 10, 10, 120));
        return hBox;
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #505050; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;");
        button.setOnAction(eventHandler);
        return button;

    }

    private void populateDepartmentComboBox() {
        String query = "SELECT departmentName FROM departments";
        ObservableList<String> departmentList = FXCollections.observableArrayList();

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                departmentList.add(resultSet.getString("departmentName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        deptComboBox.setItems(departmentList);
    }

    private int getDepartmentIdByName(String departmentName) {
        String query = "SELECT departmentId FROM departments WHERE departmentName = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, departmentName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("departmentId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if department does not exist
    }

    private boolean courseExists(int courseId) {
        String checkQuery = "SELECT COUNT(*) FROM courses WHERE courseId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(checkQuery)) {

            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void insertData() {
        String courseName = tname.getText();
        String courseCodeStr = tcode.getText();
        String deptName = deptComboBox.getValue();

        if (courseName.isEmpty() || courseCodeStr.isEmpty() || deptName == null) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
            return;
        }

        int courseId;
        try {
            courseId = Integer.parseInt(courseCodeStr);
        } catch (NumberFormatException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Course code must be a valid number.");
            return;
        }

        int departmentId = getDepartmentIdByName(deptName);

        String insertQuery = "INSERT INTO courses (courseId, courseName, courseDepartment) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertQuery)) {

            statement.setInt(1, courseId);
            statement.setString(2, courseName);
            statement.setInt(3, departmentId);
            statement.executeUpdate();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add course.");
        }
    }


    private void updateData() {
        String courseName = tname.getText();
        String courseCodeStr = tcode.getText();
        String deptName = deptComboBox.getValue();

        if (courseCodeStr.isEmpty()) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Please enter the course ID.");
            return;
        }

        int courseId = Integer.parseInt(courseCodeStr);

        if (!courseExists(courseId)) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Course does not exist.");
            return;
        }

        StringBuilder updateQuery = new StringBuilder("UPDATE courses SET ");
        boolean isFirstUpdate = true;

        if (!courseName.isEmpty()) {
            if (!isFirstUpdate) updateQuery.append(", ");
            updateQuery.append("courseName = ?");
            isFirstUpdate = false;
        }

        if (deptName != null) {
            if (!isFirstUpdate) updateQuery.append(", ");
            updateQuery.append("courseDepartment = ?");
            isFirstUpdate = false;
        }


        updateQuery.append(" WHERE courseId = ?");

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateQuery.toString())) {

            int paramIndex = 1;
            if (!courseName.isEmpty()) statement.setString(paramIndex++, courseName);
            if (deptName != null) statement.setInt(paramIndex++, getDepartmentIdByName(deptName));
            statement.setInt(paramIndex, courseId);

            statement.executeUpdate();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Course updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update course.");
        }
    }

    private void deleteData() {
        String courseCodeStr = tcode.getText();
        if (courseCodeStr.isEmpty()) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Please enter the course ID.");
            return;
        }

        int courseId = Integer.parseInt(courseCodeStr);
        String deleteQuery = "DELETE FROM courses WHERE courseId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteQuery)) {

            statement.setInt(1, courseId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Course deleted successfully.");
            } else {
                Utils.showAlert(Alert.AlertType.ERROR, "Error", "Course not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete course.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
