package com.example.project1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminStudentCharts extends Application {


    @Override
    public void start(Stage stage) {

        StackPane stackPane = new StackPane();
        // Background image
        Image backgroundImage = new Image(getClass().getResource("/university-background.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(2000);
        backgroundView.setFitHeight(1000);
        stackPane.getChildren().add(backgroundView);

        VBox root = new VBox(20);

        root.getChildren().add(createPieChart());
        root.getChildren().add(createBarChart());
        root.setStyle("-fx-padding: 20px; -fx-alignment: center;");


        // Create Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setPrefWidth(320); // Preferred button width
        backButton.setPrefHeight(60);
        backButton.setStyle("-fx-background-color: #505050; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;");
        backButton.setOnAction(e -> Utils.goToAdminDashBoardPage(stage));
        root.getChildren().add(backButton);


        stackPane.getChildren().add(root);

        // Full screen setup
        Screen primaryScreen = Screen.getPrimary();
        stackPane.setMaxWidth(primaryScreen.getVisualBounds().getWidth());
        stackPane.setMaxHeight(primaryScreen.getVisualBounds().getHeight());

        // Create the scene and set it on the stage
        Scene scene = new Scene(stackPane);
        stage.setTitle("Student Charts");
        stage.setScene(scene);
        stage.setMaximized(true); // Maximized window
        stage.show();
    }

    private PieChart createPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 10px;");

        // Lists for storing chart data and labels
        List<PieChart.Data> pieChartData = new ArrayList<>();
        Map<String, Integer> levelCounts = new HashMap<>();
        int totalStudents = 0;

        // Query to get student counts per level
        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT level, COUNT(*) AS student_count FROM students GROUP BY level")) {

            // First pass: Calculate total students and store student count per level
            while (resultSet.next()) {
                String level = resultSet.getString("level");
                int count = resultSet.getInt("student_count");
                totalStudents += count;
                levelCounts.put(level, count);
            }

            // Second pass: Add data to PieChart and calculate percentage
            for (Map.Entry<String, Integer> entry : levelCounts.entrySet()) {
                String level = entry.getKey();
                int count = entry.getValue();
                double percentage = (count / (double) totalStudents) * 100;

                PieChart.Data data = new PieChart.Data(level + " (" + String.format("%.2f", percentage) + "%)", count);
                pieChart.getData().add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pieChart;
    }

    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 10px;");
        barChart.setTitle("Average GPA by Level");
        xAxis.setLabel("Level");
        yAxis.setLabel("Average GPA");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("GPA");

        Map<String, String> levelColorMap = new HashMap<>();
        List<String> colors = List.of("#FF5733", "#33FF57", "#3357FF", "#F7DC6F", "#AF7AC5");
        int colorIndex = 0;

        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT level, AVG(gpa) AS avg_gpa FROM students GROUP BY level")) {

            while (resultSet.next()) {
                String level = resultSet.getString("level");
                double avgGpa = resultSet.getDouble("avg_gpa");

                XYChart.Data<String, Number> data = new XYChart.Data<>(level, avgGpa);
                series.getData().add(data);

                // Store the color for each level
                String color = colors.get(colorIndex % colors.size());
                levelColorMap.put(level, color);
                colorIndex++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        barChart.getData().add(series);

        // Apply colors to the bars based on the level
        barChart.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            barChart.lookupAll(".chart-bar").forEach(node -> {
                String level = (String) node.getUserData();  // Assuming 'level' is stored in userData
                String color = levelColorMap.get(level);
                if (color != null) {
                    node.setStyle("-fx-bar-fill: " + color + ";");
                }
            });
        });

        return barChart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
