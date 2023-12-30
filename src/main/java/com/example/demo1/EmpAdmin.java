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
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public  class EmpAdmin extends Application {
    private  Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        StackPane stackPane =new StackPane();
        Image backgroundImage = new Image("C:\\Users\\Ahmed\\IdeaProjects\\project1\\src\\main\\university-background.jpg");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000); // Set the width of the image as per your requirement
        backgroundView.setFitHeight(1000);
        stackPane.getChildren().add(backgroundView);
        primaryStage.setTitle("Hello employee !");




        VBox vBox =new VBox(10);

        Button stu = new Button(" Student       ");
        Button sh1 = new Button("Show");
        HBox hBox1 = new HBox(30,stu,sh1);
        hBox1.setAlignment(Pos.CENTER);
        stu.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        sh1.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        hBox1.setPadding(new Insets(70,10,10,120));
        vBox.getChildren().add(hBox1);


        Button dept = new Button("Departments");
        Button sh3 = new Button("Show");
        HBox hBox3 = new HBox(30,dept,sh3);
        hBox3.setAlignment(Pos.CENTER);
        dept.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        sh3.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        hBox3.setPadding(new Insets(30,10,10,120));
        vBox.getChildren().add(hBox3);

        Button cour = new Button("  Courses      ");
        Button sh4 = new Button("Show");
        HBox hBox4 = new HBox(30,cour,sh4);
        hBox4.setAlignment(Pos.CENTER);
        cour.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        sh4.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        hBox4.setPadding(new Insets(30,10,10,120));
        Button logout = new Button("Logout");
        logout.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");
        logout.setAlignment(Pos.CENTER);
        HBox hBox5 = new HBox(0,logout);
        hBox5.setAlignment(Pos.CENTER);
        hBox5.setPadding(new Insets(10,10,10,95));
        vBox.getChildren().add(hBox4);
        vBox.getChildren().add(hBox5);

        logout.setOnAction(actionEvent -> {
            HelloApplication helloApplication = new HelloApplication();
            Stage stage1 = new Stage();
            try {
                helloApplication.start(stage1);
                primaryStage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        stu.setOnAction(actionEvent -> {
            EmpStudent stud = new EmpStudent();
            Stage stage1 = new Stage();
            try {
                stud.start(stage1);
                primaryStage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        sh1.setOnAction(actionEvent -> {
            StudentTable stud = new StudentTable();
            Stage stage1 = new Stage();
            try {
                stud.start(stage1);
                primaryStage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        dept.setOnAction(actionEvent -> {
            EmpDepartment d = new EmpDepartment();
            Stage stage1 = new Stage();
            try {
                d.start(stage1);
                primaryStage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        sh3.setOnAction(actionEvent -> {
            DepartmentTable stud = new  DepartmentTable();
            Stage stage1 = new Stage();
            try {
                stud.start(stage1);
                primaryStage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        cour.setOnAction(actionEvent -> {
            EmpCourse d = new EmpCourse();
            Stage stage1 = new Stage();
            try {
                d.start(stage1);
                primaryStage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        sh4.setOnAction(actionEvent -> {
            CourseTable stud = new  CourseTable();
            Stage stage1 = new Stage();
            try {
                stud.start(stage1);
                primaryStage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        vBox.setAlignment(Pos.CENTER);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(vBox);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        Scene scene = new Scene(stackPane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
