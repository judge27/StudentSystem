package com.example.project1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SplashScreen extends Application {

    private static Class<? extends Application> nextAppClass;
    private static String loadingText;
    private Stage primaryStage;
    private static final int SPLASH_DURATION = 1000; // Duration in milliseconds
    private static final int TOTAL_TASKS = 100; // Number of tasks to complete
    private int completedTasks = 0;

    public static void setNextApp(Class<? extends Application> appClass, String message) {
        nextAppClass = appClass;
        loadingText = message;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Background and Icon
        Image backgroundImage = new Image(getClass().getResource("/university-background.jpg").toExternalForm()); // Replace with your image path
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000);
        backgroundView.setFitHeight(1000);
        Image iconImage = new Image(getClass().getResource("/fci_en.png").toExternalForm()); // Replace with your icon path
        ImageView iconView = new ImageView(iconImage);

        // Progress Label
        Label progressLabel = new Label(loadingText + " 0%");
        progressLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        VBox vbox = new VBox(10, iconView, progressLabel);
        vbox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(backgroundView, vbox);

        // Scene and Stage Setup
        Scene scene = new Scene(root, 800, 800);
        primaryStage.getIcons().add(iconImage);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.setTitle("Splash Screen");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        simulateLoading(progressLabel);
    }

    private void simulateLoading(Label progressLabel) {
        Thread loadingThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (completedTasks < TOTAL_TASKS) {
                completedTasks++;
                double progress = (double) completedTasks / TOTAL_TASKS * 100;
                updateLabel(progressLabel, loadingText + " " + String.format("%.0f%%", progress));

                try {
                    Thread.sleep(SPLASH_DURATION / TOTAL_TASKS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime < SPLASH_DURATION) {
                try {
                    Thread.sleep(SPLASH_DURATION - elapsedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            closeSplashScreen(primaryStage);
        });

        loadingThread.start();
    }

    private void updateLabel(Label label, String text) {
        Platform.runLater(() -> label.setText(text));
    }

    private void closeSplashScreen(Window window) {
        Platform.runLater(() -> {
            window.hide();
            try {
                Application nextApp = nextAppClass.getDeclaredConstructor().newInstance();
                Stage nextStage = new Stage();
                nextApp.start(nextStage);
                this.primaryStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        setNextApp(LoginPage.class, "Loading Login Screen");
        launch(args);
    }
}
