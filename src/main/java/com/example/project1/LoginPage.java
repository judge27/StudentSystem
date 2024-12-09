package com.example.project1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class LoginPage extends Application {
    public  static String role;
    private Button loginButton;
    private TextField usernameInput;
    private PasswordField passwordInput;
    public static String currentusername = "";
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Login");

        // Set the main window icon
        Image icon = new Image(getClass().getResource("/loginsymbol.png").toExternalForm());
        primaryStage.getIcons().add(icon);

        // Set the background image
        Image backgroundImage = new Image(getClass().getResource("/university-background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000);
        backgroundView.setFitHeight(1000);

        // Create the StackPane and set the background
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(backgroundView);

        // Create the login button and set up its style and functionality
        loginButton = new Button("Login");
        loginButton.setStyle("-fx-text-fill: black; -fx-background-color: #817C7B; -fx-font-size:15; -fx-font-weight: bold;");
        loginButton.setDisable(true);
        loginButton.setOnAction(e -> onLogin());

        // Create the input fields for username and password
        usernameInput = new TextField();
        usernameInput.setPromptText("Enter your username");
        usernameInput.setMinWidth(250);
        usernameInput.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        usernameInput.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter your password");
        passwordInput.setMinWidth(250);
        passwordInput.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        passwordInput.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Labels for the input fields
        Label usernameLabel = new Label("Student Id:");
        usernameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create a GridPane for layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameInput, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordInput, 1, 1);
        gridPane.add(loginButton, 1, 2);
        gridPane.setAlignment(Pos.CENTER);

        // Add the gridPane to the stackPane
        stackPane.getChildren().add(gridPane);

        // Position the top-right logo
        Image topRightLogo = new Image(getClass().getResource("/fci_en.png").toExternalForm());
        ImageView topRightView = new ImageView(topRightLogo);
        StackPane.setAlignment(topRightView, Pos.TOP_RIGHT);
        stackPane.getChildren().add(topRightView);

        // Position the bottom-left logo
        Image bottomLeftLogo = new Image(getClass().getResource("/uni_en.png").toExternalForm());
        ImageView bottomLeftView = new ImageView(bottomLeftLogo);
        StackPane.setAlignment(bottomLeftView, Pos.BOTTOM_LEFT);
        stackPane.getChildren().add(bottomLeftView);

        // Set the window bounds
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);

        // Set up the scene
        Scene scene = new Scene(stackPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void checkFields() {
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        boolean isFilled = !username.isEmpty() && !password.isEmpty();
        loginButton.setDisable(!isFilled);
        if (isFilled) {
            Platform.runLater(() -> loginButton.setStyle("-fx-background-color: #336699; -fx-text-fill: white; -fx-font-size:15; -fx-font-weight: bold;"));
        } else {
            Platform.runLater(() -> loginButton.setStyle("-fx-text-fill: white; -fx-background-color: #817C7B; -fx-font-size:15; -fx-font-weight: bold;"));
        }
    }

    private void onLogin() {
        String id = usernameInput.getText();
        String password = passwordInput.getText();
        currentusername = id;

        // Query to fetch the password and role for the user
        String query = "SELECT password, role FROM authentication WHERE id = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.isBeforeFirst()) {
                Utils.showAlert(Alert.AlertType.ERROR, "Login Error", "Please Enter Required Data");
            } else {
                while (rs.next()) {
                    String retrievedPassword = rs.getString("password");
                role = rs.getString("role");

                    if (retrievedPassword.equals(password)) {
                        // Check role and navigate to the appropriate dashboard
                        if ("admin".equalsIgnoreCase(role)) {
                            goToAdminDashboard();
                        } else {
                            goToStudentDashboard();
                        }
                        primaryStage.close();
                    } else {
                        Utils.showAlert(Alert.AlertType.ERROR, "Login Error", "Username or password is incorrect");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void goToAdminDashboard() {
        AdminDashboard adminDashboardPage = new AdminDashboard();
        adminDashboardPage.start(new Stage());
        primaryStage.close();
    }

    private void goToStudentDashboard() {
        StudentDashboard dashboardPage = new StudentDashboard();
        dashboardPage.start(new Stage());
        primaryStage.close();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
