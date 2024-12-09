package com.example.project1;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Utils {


    public static void goToStudentDashBoardPage(Stage primaryStage) {
        StudentDashboard dashboardPage = new StudentDashboard();
        dashboardPage.start(new Stage());
        primaryStage.close();
    }
    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    public static void goToAdminDashBoardPage(Stage primaryStage) {
        try {
            AdminDashboard dashboardPage = new AdminDashboard();
            dashboardPage.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout(Button button,Stage primaryStage) {
        LoginPage loginApp =new LoginPage();
        loginApp.start(new Stage());
        primaryStage.close();

    }

}
