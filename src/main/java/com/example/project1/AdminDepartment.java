package com.example.project1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminDepartment extends Application {

    private Stage primaryStage;
    private TextField tDepartmentId;
    private TextField tDepartmentName;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        StackPane stackPane = createBackgroundStackPane();

        primaryStage.setTitle("Department Management");

        // Create main UI grid pane
        GridPane gridPane = createGridPane();
        stackPane.getChildren().add(gridPane);

        // Set full-screen mode
        setFullScreen(primaryStage);

        Scene scene = new Scene(stackPane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createBackgroundStackPane() {
        StackPane stackPane = new StackPane();
        Image backgroundImage = new Image(getClass().getResource("/university-background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000);
        backgroundView.setFitHeight(1000);
        stackPane.getChildren().add(backgroundView);
        return stackPane;
    }

    private void setFullScreen(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
    }

    // Create UI layout with reusable components
    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);

        // Department ID
        tDepartmentId = createTextFieldWithLabel("Department ID:", gridPane, 0);

        // Department Name
        tDepartmentName = createTextFieldWithLabel("Department Name:", gridPane, 1);

        // Buttons
        HBox buttonBox = createButtonBox();
        gridPane.add(buttonBox, 0, 2, 2, 1);

        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private TextField createTextFieldWithLabel(String labelText, GridPane gridPane, int rowIndex) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
        TextField textField = new TextField();
        gridPane.addRow(rowIndex, label, textField);
        return textField;
    }

    private HBox createButtonBox() {
        Button addButton = createStyledButton("Add Department", e -> insertData());
        Button updateButton = createStyledButton("Update Department", e -> updateData());
        Button deleteButton = createStyledButton("Delete Department", e -> deleteData());
        Button backButton = createStyledButton("Back", e -> Utils.goToAdminDashBoardPage(primaryStage));

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
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

    // Insert department record
    private void insertData() {
        String departmentId = tDepartmentId.getText();
        String departmentName = tDepartmentName.getText();

        String query = "INSERT INTO departments (departmentId, departmentName) VALUES (?, ?)";

        try {
            int rows = DatabaseHelper.executeUpdate(query, Integer.parseInt(departmentId), departmentName);
            if (rows > 0) {
                Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Department added successfully.");
            }
        } catch (Exception e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add department: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update department record
    private void updateData() {
        String departmentId = tDepartmentId.getText();
        String departmentName = tDepartmentName.getText();

        String query = "UPDATE departments SET departmentName = ? WHERE departmentId = ?";

        try {
            int rows = DatabaseHelper.executeUpdate(query, departmentName, Integer.parseInt(departmentId));
            if (rows > 0) {
                Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Department updated successfully.");
            } else {
                Utils.showAlert(Alert.AlertType.WARNING, "Warning", "No department found with the given ID.");
            }
        } catch (Exception e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update department: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete department record
    private void deleteData() {
        String departmentId = tDepartmentId.getText();

        String query = "DELETE FROM departments WHERE departmentId = ?";

        try {
            int rows = DatabaseHelper.executeUpdate(query, Integer.parseInt(departmentId));
            if (rows > 0) {
                Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Department deleted successfully.");
            } else {
                Utils.showAlert(Alert.AlertType.WARNING, "Warning", "No department found with the given ID.");
            }
        } catch (Exception e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete department: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
