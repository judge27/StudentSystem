package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;

import java.sql.*;

import javafx.stage.Stage;

import java.io.IOException;

//import static jdk.internal.org.jline.terminal.Terminal.MouseTracking.Button;

public class HelloApplication extends Application {
    private Stage primaryStage;
    static  String myrole;

    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        StackPane stackPane = new StackPane();
        Image backgroundImage = new Image("C:\\Users\\Ahmed\\IdeaProjects\\project1\\src\\main\\university-background.jpg");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000); // Set the width of the image as per your requirement
        backgroundView.setFitHeight(1000);
        stackPane.getChildren().add(backgroundView);
        GridPane gridPane = new GridPane();
//        gridPane.setStyle("-fx-background-color:#0F1011");
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);

        Label login = new Label("Login ");
        login.setStyle("-fx-font-size: 25;-fx-font-weight: bold; -fx-text-fill: black");
        login.setPadding(new Insets(10, 10, 10, 110));
        gridPane.add(login, 0, 0);


        Label user = new Label("User Name ");
        user.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        TextField tuser = new TextField();
        HBox hBox1 = new HBox(5, user, tuser);
        hBox1.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(hBox1, 0, 1);

        Label pass = new Label("Password   ");
        pass.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        PasswordField tpass = new PasswordField();
        HBox hBox2 = new HBox(5, pass, tpass);
        hBox2.setPadding(new Insets(10, 10, 10, 12));
        gridPane.add(hBox2, 0, 2);

        Button loginButton = new Button("Login");
        gridPane.add(loginButton, 0, 3);
//        loginButton.setPadding(new Insets(10));
        loginButton.setStyle("-fx-font-size: 15;-fx-font-weight: bold; -fx-text-fill: Black");
        HBox hBox3 = new HBox(5, loginButton);
        hBox3.setPadding(new Insets(10, 10, 10, 120));
        gridPane.add(hBox3, 0, 3);

        loginButton.setOnAction(actionEvent -> {
            String enteredUser = tuser.getText();
            String enteredPass = tpass.getText();

            // Check the credentials against the database
            checkCredentials(enteredUser, enteredPass);

        });


        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        gridPane.setAlignment(Pos.CENTER);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(gridPane);
        Scene scene = new Scene(stackPane, 1000, 800);
        primaryStage.setTitle("Login to system admin !");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    String url = "jdbc:mysql://localhost:3306/studentsystem";
    String user = "root";
    String pass = "";

    // Method to check credentials in the database
    private void checkCredentials(String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement statement = conn.createStatement()) {

            String query = "SELECT id, password, role FROM login WHERE id = '" + username + "' AND password = '" + password + "'";
            ResultSet rs = statement.executeQuery(query);

            if (!rs.isBeforeFirst()) {
                System.out.println("User Not Found");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username or password is incorrect");
                alert.show();
            } else {
                while (rs.next()) {
                    String retrievedPassword = rs.getString("password");
                    myrole = rs.getString("role"); // Fetch role from the result set

                    if (retrievedPassword.equals(password) && myrole.equals("admin")) {
                        Admin admin = new Admin();
                        admin.start(new Stage());
                        primaryStage.close();
                        // Open new stage or perform further actions here upon successful login
                    } else if (retrievedPassword.equals(password) && !myrole.equals("admin")) {
                        EmpAdmin admin = new EmpAdmin();
                        admin.start(new Stage());
                        primaryStage.close();
                        // Open new stage or perform further actions here upon successful login
                    } else {
                        System.out.println("Password incorrect");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Username or password is incorrect");
                        alert.show();
                    }
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}