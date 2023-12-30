package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public  class EmpCourse extends Application {
    private  Stage primaryStage;
    private TextField tname;
    private TextField tcode;
    private  TextField tdept;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        StackPane stackPane =new StackPane();
        Image backgroundImage = new Image("C:\\Users\\Ahmed\\IdeaProjects\\project1\\src\\main\\university-background.jpg");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000); // Set the width of the image as per your requirement
        backgroundView.setFitHeight(1000);
        stackPane.getChildren().add(backgroundView);
        primaryStage.setTitle("Course !");



        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);

        Label name = new Label("Name           ");
        name.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
         tname = new TextField();
        HBox hBox1 = new HBox(10,name,tname);
        hBox1.setPadding(new Insets(30,10,10,120));
        gridPane.add(hBox1,0,0);

        Label code = new Label("Code            ");
        code.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
         tcode = new TextField();
        HBox hBox2 = new HBox(10,code,tcode);
        hBox2.setPadding(new Insets(30,10,10,120));
        gridPane.add(hBox2,0,1);

        Label dept = new Label("Department");
        dept.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
         tdept = new TextField();
        HBox hBox3 = new HBox(10,dept,tdept);
        hBox3.setPadding(new Insets(30,10,10,120));
        gridPane.add(hBox3,0,2);

        Button add = new Button("  Add  ");
        add.setOnAction(event -> {
            insertData();
        });
        Button back =new Button("    Back    ");
        back.setOnAction(event -> {
            EmpAdmin admin =new EmpAdmin();
            try {
                admin.start(new Stage());
                primaryStage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        add.setStyle("-fx-font-size: 15;-fx-font-weight: bold; -fx-text-fill: black");
        back.setStyle("-fx-font-size: 15;-fx-font-weight: bold; -fx-text-fill: black");
        HBox hbox4 = new HBox(31,add,back);
        hbox4.setPadding(new Insets(30,10,10,230));
        gridPane.add(hbox4,0,3);

        gridPane.setAlignment(Pos.CENTER);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(gridPane);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        Scene scene = new Scene(stackPane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    String url = "jdbc:mysql://localhost:3306/studentsystem";
    String user = "root";
    String password = "";
    private boolean hasInserted = false; // Track whether insertion occurred

    private void insertData() {
        // Check if insertion already occurred
        if (hasInserted) {
            // Display a message or handle as needed
            System.out.println("Record already inserted!");
            return;
        }

        // Get values from UI elements or use dummy data
        String Name = tname.getText();
        int Code = Integer.parseInt(tcode.getText()); // Parse string code to an integer
        String Dept = tdept.getText();


        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement statement = conn.createStatement()) {

            String insertQuery = "INSERT INTO courses (name, code, department) " +
                    "VALUES ('" + Name + "', '" + Code + "', '" + Dept + "')";


            statement.executeUpdate(insertQuery);
            // Refresh the table view after insertion

            hasInserted = true; // Set the flag to indicate insertion occurred

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
