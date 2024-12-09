package com.example.project1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminDashboard extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Root layout
        StackPane stackPane = new StackPane();

        // Background image
        Image backgroundImage = new Image(getClass().getResource("/university-background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000);
        backgroundView.setFitHeight(1000);

        // Buttons with unified styles
        Button studentButton = createStyledButton("Student Management");
        Button departmentButton = createStyledButton("Departments Management");
        Button courseButton = createStyledButton("Courses Management");
        Button studentCoursesButton = createStyledButton("Student Courses Management");
        Button studentChartsButton = createStyledButton("Student Charts Visualizing");
        Button studentTopTenByGpaButton = createStyledButton("Top Students by GPA");
        Button logoutButton = createStyledButton("Logout");

        // Set actions for buttons
        setButtonActions(studentButton, departmentButton, courseButton, studentCoursesButton, studentChartsButton, studentTopTenByGpaButton, logoutButton);

        // Arrange buttons in a VBox
        VBox menuButtons = new VBox(20); // Spacing between buttons
        menuButtons.setPadding(new Insets(40, 0, 40, 0)); // Padding for VBox
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.getChildren().addAll(
                studentButton,
                departmentButton,
                courseButton,
                studentCoursesButton,
                studentChartsButton,
                studentTopTenByGpaButton,
                logoutButton
        );

        // Add background and menu to the layout
        stackPane.getChildren().addAll(backgroundView, menuButtons);

        // Set the scene with full screen size
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        Scene scene = new Scene(stackPane, 800, 600);

        // Set stage properties
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a styled button with consistent design.
     *
     * @param text Button label
     * @return Styled Button
     */
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

    /**
     * Sets actions for all buttons.
     */
    private void setButtonActions(Button studentButton, Button departmentButton, Button courseButton,
                                  Button studentCoursesButton, Button studentChartsButton, Button studentTopTenByGpaButton, Button logoutButton) {

        studentButton.setOnAction(event -> openNewWindow(new AdminStudent()));
        departmentButton.setOnAction(event -> openNewWindow(new AdminDepartment()));
        courseButton.setOnAction(event -> openNewWindow(new AdminCourses()));
        studentCoursesButton.setOnAction(event -> openNewWindow(new AdminStudentCourses()));
        studentChartsButton.setOnAction(event -> openNewWindow(new AdminStudentCharts()));
        studentTopTenByGpaButton.setOnAction(event -> openNewWindow(new TopStudentsByGPA()));

        logoutButton.setOnAction(event -> Utils.logout(logoutButton, primaryStage));
    }

    /**
     * Opens a new stage for the given application.
     */
    private void openNewWindow(Application app) {
        try {
            Stage newStage = new Stage();
            app.start(newStage);
            primaryStage.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to open new window", e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
