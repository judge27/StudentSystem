package com.example.project1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;

import static com.example.project1.LoginPage.currentusername;

public class StudentDetailsPage extends Application {

    private Stage primaryStage;
    private final ObservableList<StudentModel> students = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupStage();

        // Create and configure the table
        TableView<StudentModel> table = createStudentTable();
        fetchDataFromDatabase();
        table.setItems(students);

        // Create and configure the back button
        Button backButton = createStyledButton("Back To Dashboard");
        setBackButtonAction(backButton);

        // Stack pane layout
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(table, backButton);

        // Scene setup
        setupScene(stackPane, table);

        primaryStage.show();
    }

    private void setupStage() {
        Image icon = new Image(getClass().getResource("/icon.png").toExternalForm());
        primaryStage.getIcons().add(icon);
    }

    private TableView<StudentModel> createStudentTable() {
        TableView<StudentModel> table = new TableView<>();
        table.setRowFactory(tv -> new TableRow<StudentModel>() {
            @Override
            protected void updateItem(StudentModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    getStyleClass().add("table-row-hidden");
                } else {
                    getStyleClass().remove("table-row-hidden");
                }
            }
        });

        // Create individual columns with appropriate property accessors
        TableColumn<StudentModel, Integer> studentIdColumn = new TableColumn<>("Student_ID");
        studentIdColumn.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty().asObject());
        studentIdColumn.setPrefWidth(170);

        TableColumn<StudentModel, String> studentNameColumn = new TableColumn<>("Student Name");
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        studentNameColumn.setPrefWidth(170);

        TableColumn<StudentModel, Integer> studentLevelColumn = new TableColumn<>("Student Level");
        studentLevelColumn.setCellValueFactory(cellData -> cellData.getValue().studentLevelProperty().asObject());
        studentLevelColumn.setPrefWidth(170);

        TableColumn<StudentModel, Float> studentGpaColumn = new TableColumn<>("Student GPA");
        studentGpaColumn.setCellValueFactory(cellData -> cellData.getValue().studentGpaProperty().asObject());
        studentGpaColumn.setPrefWidth(170);

        TableColumn<StudentModel, String> studentNationalColumn = new TableColumn<>("Student National");
        studentNationalColumn.setCellValueFactory(cellData -> cellData.getValue().stduentNationalProperty());
        studentNationalColumn.setPrefWidth(170);

        TableColumn<StudentModel, String> studentNationalIdColumn = new TableColumn<>("Student NationalID");
        studentNationalIdColumn.setCellValueFactory(cellData -> cellData.getValue().studentNationalIdProperty());
        studentNationalIdColumn.setPrefWidth(170);

        TableColumn<StudentModel, String> genderColumn = new TableColumn<>("Student Gender");
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        genderColumn.setPrefWidth(170);

        TableColumn<StudentModel, String> studentBirthdateColumn = new TableColumn<>("Student Birthdate");
        studentBirthdateColumn.setCellValueFactory(cellData -> cellData.getValue().studentBirthdateProperty());
        studentBirthdateColumn.setPrefWidth(170);

        TableColumn<StudentModel, String> departmentNameColumn = new TableColumn<>("Department");
        departmentNameColumn.setCellValueFactory(cellData -> cellData.getValue().departmentNameProperty());
        departmentNameColumn.setPrefWidth(170);

        // Add all columns to the table
        table.getColumns().addAll(studentIdColumn, studentNameColumn, studentLevelColumn, studentGpaColumn,
                studentNationalColumn, studentNationalIdColumn, genderColumn, studentBirthdateColumn, departmentNameColumn);

        return table;
    }




    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(320);
        button.setPrefHeight(60);
        button.setStyle("-fx-background-color: #505050; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px;");
        return button;
    }

    private void setBackButtonAction(Button backButton) {
        backButton.setOnAction(e -> Utils.goToStudentDashBoardPage(primaryStage));
    }

    private void setupScene(StackPane stackPane, TableView<StudentModel> table) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());

        Scene scene = new Scene(stackPane, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        table.getStyleClass().add("table-background");
        table.getStyleClass().add("table-cell-centered");

        primaryStage.setTitle("Student Details");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
    }

    private void fetchDataFromDatabase() {
        String query = """
                SELECT s.studentId, s.studentName, s.level, s.gpa, s.national, s.nationalId, s.gender, s.birthDate, d.departmentName
                FROM students s
                INNER JOIN departments d ON s.departmentId = d.departmentId
                WHERE s.studentId = ?
                """;

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setString(1, currentusername); // Fetch data based on the logged-in student

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(new StudentModel(
                            resultSet.getInt("studentId"),
                            resultSet.getString("studentName"),
                            resultSet.getInt("level"),
                            resultSet.getFloat("gpa"),
                            resultSet.getString("national"),
                            resultSet.getString("nationalId"),
                            resultSet.getString("gender"),
                            resultSet.getString("birthDate"),
                            resultSet.getString("departmentName")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
