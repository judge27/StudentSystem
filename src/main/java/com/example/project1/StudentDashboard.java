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

public class StudentDashboard extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        this.primaryStage = primaryStage;
        Image backgroundImage =new Image(getClass().getResource("/university-background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000); // Set the width of the image as per your requirement
        backgroundView.setFitHeight(1000);

        // Buttons with unified styles
        Button studentDetailsButton = createStyledButton("Student Details");
        Button calculateGpaButton = createStyledButton("Calculate GPA");
        Button registerdCoursesButton = createStyledButton("Registerd Courses");
        Button logoutButton = createStyledButton("Logout");

        // Set actions for buttons
        setButtonActions(studentDetailsButton, calculateGpaButton, registerdCoursesButton, logoutButton);

        // Arrange buttons in a VBox
        VBox menuButtons = new VBox(20); // Spacing between buttons
        menuButtons.setPadding(new Insets(40, 0, 40, 0)); // Padding for VBox
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.getChildren().addAll(
                studentDetailsButton,
                calculateGpaButton,
                registerdCoursesButton,
                logoutButton
        );






        root.getChildren().addAll(backgroundView, menuButtons);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Student Dashboard");
        primaryStage.setMaximized(true); // Maximize on start
        primaryStage.setResizable(true); // Allow resizing

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setPrefWidth(200); // Set preferred width
        button.setPrefHeight(60);
        button.setOnAction(handler);
        button.setStyle("-fx-background-color: grey; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 10; " +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;");
        return button;
    }

    private void handleExtraAction() {
        // Handle the action of the extra button
        System.out.println("Extra button action performed!");
    }



    private void goToStudentSplashScreen() {
        SplashScreen.setNextApp(StudentDetailsPage.class, "Loading Student Page");
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start(new Stage());
        primaryStage.close();
    }
    private void GpaCalculator() {
        SplashScreen.setNextApp(StudentGpaCalculatorPage.class, "Loading GPA Page");
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start(new Stage());
        primaryStage.close();

    }
    private void setButtonActions(Button studentDetailsButton, Button calculateGpaButton, Button registerdCoursesButton,
                                 Button logoutButton) {

        studentDetailsButton.setOnAction(event -> openNewWindow(new StudentDetailsPage()));
        calculateGpaButton.setOnAction(event -> openNewWindow(new StudentGpaCalculatorPage()));
        registerdCoursesButton.setOnAction(event -> openNewWindow(new StudentCoursePage()));

        logoutButton.setOnAction(event -> Utils.logout(logoutButton, primaryStage));
    }


    private void goToCoursesCodes() {
        SplashScreen.setNextApp(StudentCoursePage.class, "Loading Course Page");
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start(new Stage());
        primaryStage.close();

    }
    private void openNewWindow(Application app) {
        try {
            Stage newStage = new Stage();
            app.start(newStage);
            primaryStage.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to open new window", e);
        }
    }
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

    public static void main(String[] args) {
        launch(args);
}
}