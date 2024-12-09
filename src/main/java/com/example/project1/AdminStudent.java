package com.example.project1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.sql.*;
import java.util.*;

public class AdminStudent extends Application {

    private Stage primaryStage;
    private TextField tname, tcode, tgpa, tnationalId, tnationality, tbirthDate;
    private ComboBox<String> tdept;
    private ComboBox<Integer> tlevel;
    private ComboBox<String> tgender;
    private PasswordField tpassword;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(createBackgroundImage());
        primaryStage.setTitle("Student Management");

        GridPane gridPane = createGridPane();
        stackPane.getChildren().add(gridPane);

        setPrimaryStageProperties(primaryStage);
        Scene scene = new Scene(stackPane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Set primary stage properties
    private void setPrimaryStageProperties(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        addTextFieldToGrid(gridPane, "Name", 0, tname = new TextField());
        addTextFieldToGrid(gridPane, "Student ID", 1, tcode = new TextField());
        addPasswordFieldToGrid(gridPane, "Password", 2);
        addTextFieldToGrid(gridPane, "National ID", 3, tnationalId = new TextField());
        addTextFieldToGrid(gridPane, "Nationality", 4, tnationality = new TextField());
        addTextFieldToGrid(gridPane, "Birth Date (YYYY-MM-DD)", 5, tbirthDate = new TextField());
        addTextFieldToGrid(gridPane, "GPA", 6, tgpa = new TextField());
        addComboBoxToGrid(gridPane, "Level", 7, tlevel = new ComboBox<>());
        addComboBoxToGrid(gridPane, "Department", 8, tdept = new ComboBox<>());
        loadDepartments();


        tlevel.getItems().addAll(1, 2, 3, 4);


        addComboBoxToGrid(gridPane, "Gender", 9, tgender = new ComboBox<>());
        tgender.getItems().addAll("Male", "Female");



        addActionButtons(gridPane);

        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private void addTextFieldToGrid(GridPane grid, String labelText, int row, TextField textField) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");

        grid.addRow(row, label, textField);
    }

    private void addComboBoxToGrid(GridPane grid, String labelText, int row, ComboBox<?> comboBox) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");

        grid.addRow(row, label, comboBox);
    }

    private void addPasswordFieldToGrid(GridPane grid, String labelText, int row) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: black");

        tpassword = new PasswordField();
        grid.addRow(row, label, tpassword);
    }

    private void addActionButtons(GridPane grid) {
        Button addButton = createActionButton("Add Student", this::insertData);
        Button updateButton = createActionButton("Update Student", this::updateData);
        Button deleteButton = createActionButton("Delete Student", this::deleteData);
        Button backButton = new Button("Back");
        styleButtons(addButton, updateButton, deleteButton, backButton);

        backButton.setOnAction(e -> Utils.goToAdminDashBoardPage(primaryStage));
        HBox actionButtons = new HBox(10, addButton, updateButton, deleteButton, backButton);
        actionButtons.setAlignment(Pos.CENTER);
        grid.add(actionButtons, 0, 12, 2, 1);
    }

    private Button createActionButton(String label, Runnable action) {
        Button button = new Button(label);
        button.setOnAction(e -> action.run());
        return button;
    }

    private void styleButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #505050; " +
                    "-fx-text-fill: white; " +
                    "-fx-border-radius: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-font-size: 18px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 10px;");
        }
    }

    // Load departments from the database into the ComboBox
    private void loadDepartments() {
        String query = "SELECT departmentName FROM departments";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            var resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String departmentName = resultSet.getString("departmentName");
                tdept.getItems().add(departmentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Failed to load departments.");
        }
    }
    private void insertData() {
        // Validate that all required fields are filled
        if (tname.getText().isEmpty() || tcode.getText().isEmpty() || tdept.getValue() == null || tlevel.getValue() == null ||
                tgpa.getText().isEmpty() || tnationalId.getText().isEmpty() || tnationality.getText().isEmpty() ||
                tbirthDate.getText().isEmpty() || tgender.getValue() == null || tpassword.getText().isEmpty()) {

            // Show an alert if any field is empty
            Utils.showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all the fields.");
            return;
        }

        String name = tname.getText();
        int studentId = Integer.parseInt(tcode.getText());
        String departmentName = tdept.getValue();
        int level = tlevel.getValue();
        String gpaText = tgpa.getText();
        String nationalId = tnationalId.getText();
        String nationality = tnationality.getText();
        String birthDate = tbirthDate.getText();
        String gender = tgender.getValue();
        String password = tpassword.getText();
        if (isNationalIdRegistered(nationalId)) {
            Utils.showAlert(Alert.AlertType.WARNING, "Duplicate National ID", "This National ID is already registered.");
            return;
        }

        if (!isValidNationalId(nationalId)) {
            Utils.showAlert(Alert.AlertType.ERROR, "Invalid National ID",
                    "National ID must be exactly 14 numeric characters.");
            return;
        }
        if (!isValidGPA(tgpa.getText())) {
            Utils.showAlert(Alert.AlertType.ERROR, "Invalid GPA",
                    "GPA must be a numeric value between 0.0 and 4.0.");
            return;
        }
        if (!isValidNationality(nationality)) {
            Utils.showAlert(Alert.AlertType.ERROR, "Invalid Nationality",
                    "Nationality must contain only alphabetic characters.");
            return;
        }

        // Fetch departmentId from departmentName
        int departmentId = getDepartmentId(departmentName);
        float gpa =Float.parseFloat(tgpa.getText());
        String authQuery = "INSERT IGNORE INTO authentication (id, password, role) VALUES (?, ?, ?)";
        String studentQuery = "INSERT INTO students (studentId, studentName, departmentId, level, gpa, nationalId, national, birthDate, gender) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseHelper.getConnection()) {
            connection.setAutoCommit(false);

            // Insert into authentication table
            try (PreparedStatement authStatement = connection.prepareStatement(authQuery)) {
                authStatement.setInt(1, studentId);
                authStatement.setString(2, password);
                authStatement.setString(3, "user");
                authStatement.executeUpdate();
            }

            // Insert into students table
            try (PreparedStatement studentStatement = connection.prepareStatement(studentQuery)) {
                studentStatement.setInt(1, studentId);
                studentStatement.setString(2, name);
                studentStatement.setInt(3, departmentId);
                studentStatement.setInt(4, level);
                studentStatement.setFloat(5, gpa);
                studentStatement.setString(6, nationalId);
                studentStatement.setString(7, nationality);
                studentStatement.setString(8, birthDate);
                studentStatement.setString(9, gender);

                int rowsAffected = studentStatement.executeUpdate();
                if (rowsAffected > 0) {
                    connection.commit();
                    Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully.");
                } else {
                    throw new SQLException("Failed to insert student record.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            try (Connection connection = DatabaseHelper.getConnection()) {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }


    // Get departmentId based on departmentName
    private int getDepartmentId(String departmentName) {
        String query = "SELECT departmentId FROM departments WHERE departmentName = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, departmentName);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("departmentId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return an invalid ID if department not found
    }

    // Update student data (placeholder for update logic)
    private void updateData() {
        // Validate that the studentId is provided
        if (tcode.getText().isEmpty()) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a Student ID.");
            return;
        }

        int studentId = Integer.parseInt(tcode.getText());

        // Check if the student ID exists in the database
        if (!isStudentExist(studentId)) {
            Utils.showAlert(Alert.AlertType.WARNING, "Student Not Found", "This student doesn't exist in our system.");
            return;
        }
        String name = tname.getText();
        String departmentName = tdept.getValue();
        Integer level = tlevel.getValue();
        String gpa = tgpa.getText();
        String nationalId = tnationalId.getText();
        String nationality = tnationality.getText();
        String birthDate = tbirthDate.getText();
        String gender = tgender.getValue();
        String password = tpassword.getText();

        // Fetch departmentId based on departmentName if department is selected
        int departmentId = departmentName != null ? getDepartmentId(departmentName) : -1;

        if (!nationalId.isEmpty() && isNationalIdRegistered(nationalId)) {
            Utils.showAlert(Alert.AlertType.WARNING, "Duplicate National ID", "This National ID is already registered.");
            return;
        }

        if (!nationalId.isEmpty() && !isValidNationalId(nationalId)) {
            Utils.showAlert(Alert.AlertType.ERROR, "Invalid National ID",
                    "National ID must be exactly 14 numeric characters.");
            return;
        }

        if (!gpa.isEmpty() && !isValidGPA(tgpa.getText())) {
            Utils.showAlert(Alert.AlertType.ERROR, "Invalid GPA",
                    "GPA must be a numeric value between 0.0 and 4.0.");
            return;
        }


        if (!nationality.isEmpty() && !isValidNationality(nationality)) {
            Utils.showAlert(Alert.AlertType.ERROR, "Invalid Nationality",
                    "Nationality must contain only alphabetic characters.");
            return;
        }



        // Prepare the update query with optional fields
        StringBuilder queryBuilder = new StringBuilder("UPDATE students SET ");
        List<Object> parameters = new ArrayList<>();
        boolean isFirstField = true;

        // Add name to the update query if not empty
        if (!name.isEmpty()) {
            appendSetField(queryBuilder, isFirstField, "studentName = ?");
            parameters.add(name);
            isFirstField = false;
        }

        // Add departmentId to the update query if a department is selected
        if (departmentId != -1) {
            appendSetField(queryBuilder, isFirstField, "departmentId = ?");
            parameters.add(departmentId);
            isFirstField = false;
        }

        // Add level to the update query if a level is selected
        if (level != null) {
            appendSetField(queryBuilder, isFirstField, "level = ?");
            parameters.add(level);
            isFirstField = false;
        }

        // Add GPA to the update query if entered
        if (!gpa.isEmpty()) {
            appendSetField(queryBuilder, isFirstField, "gpa = ?");
            parameters.add(Float.parseFloat(gpa));
            isFirstField = false;
        }

        // Add nationalId to the update query if entered
        if (!nationalId.isEmpty()) {
            appendSetField(queryBuilder, isFirstField, "nationalId = ?");
            parameters.add(nationalId);
            isFirstField = false;
        }

        // Add nationality to the update query if entered
        if (!nationality.isEmpty()) {
            appendSetField(queryBuilder, isFirstField, "national = ?");
            parameters.add(nationality);
            isFirstField = false;
        }

        // Add birthDate to the update query if entered
        if (!birthDate.isEmpty()) {
            appendSetField(queryBuilder, isFirstField, "birthDate = ?");
            parameters.add(birthDate);
            isFirstField = false;
        }

        // Add gender to the update query if selected
        if (gender != null) {
            appendSetField(queryBuilder, isFirstField, "gender = ?");
            parameters.add(gender);
            isFirstField = false;
        }

        // If any field is updated, finalize the query with studentId condition
        if (queryBuilder.length() > 17) {  // "UPDATE students SET " is 17 characters
            queryBuilder.append(" WHERE studentId = ?");
            parameters.add(studentId);

            try (Connection connection = DatabaseHelper.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(queryBuilder.toString())) {

                // Set the prepared statement parameters
                for (int i = 0; i < parameters.size(); i++) {
                    stmt.setObject(i + 1, parameters.get(i));
                }

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully.");
                } else {
                    Utils.showAlert(Alert.AlertType.ERROR, "Error", "Student update failed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Utils.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            }
        } else {
            Utils.showAlert(Alert.AlertType.WARNING, "No Changes", "No fields were updated.");
        }
    }

    // Helper method to append fields to the query
    private void appendSetField(StringBuilder queryBuilder, boolean isFirstField, String field) {
        if (isFirstField) {
            queryBuilder.append(field);
        } else {
            queryBuilder.append(", ").append(field);
        }
    }



    // Delete student data (placeholder for delete logic)
    private void deleteData() {
        String studentIdText = tcode.getText(); // Get the student ID from the input field

        // Check if student ID field is empty
        if (studentIdText.isEmpty()) {
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a Student ID.");
            return;
        }

        int studentId = Integer.parseInt(studentIdText);

        // Check if the student ID exists in the database
        if (!isStudentExist(studentId)) {
            Utils.showAlert(Alert.AlertType.WARNING, "Student Not Found", "This student doesn't exist in our system.");
            return;
        }

        try (Connection connection = DatabaseHelper.getConnection()) {
            connection.setAutoCommit(false);

            // Delete from students table
            String studentQuery = "DELETE FROM students WHERE studentId = ?";
            try (PreparedStatement studentStatement = connection.prepareStatement(studentQuery)) {
                studentStatement.setInt(1, studentId);
                studentStatement.executeUpdate();
            }

            // Delete from authentication table
            String authQuery = "DELETE FROM authentication WHERE id = ?";
            try (PreparedStatement authStatement = connection.prepareStatement(authQuery)) {
                authStatement.setInt(1, studentId);
                authStatement.executeUpdate();
            }

            connection.commit();
            Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Student deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }
    private boolean isNationalIdRegistered(String nationalId) {
        String query = "SELECT COUNT(*) FROM students WHERE nationalId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nationalId);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Return true if the nationalId exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if the nationalId does not exist
    }
    private boolean isValidNationalId(String nationalId) {
        return nationalId.matches("\\d{14}"); // Matches exactly 14 numeric characters
    }


    // Helper method to check if the student exists in the database
    private boolean isStudentExist(int studentId) {
        String query = "SELECT COUNT(*) FROM students WHERE studentId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Return true if the student exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if the student does not exist
    }

    private boolean isValidGPA(String gpaText) {
        try {
            float gpa = Float.parseFloat(gpaText);
            if ((gpa >= 0.0 && gpa <= 4.0) && gpaText.matches("\\d+(\\.\\d+)?"))
                return true;
        } catch (NumberFormatException e) {
            return false; // If parsing fails, return false
        }
        return false;
    }
    private boolean isValidNationality(String nationality) {
        return nationality.matches("[a-zA-Z]+");
    }



    // Create background image (placeholder)
    private ImageView createBackgroundImage() {
        Image backgroundImage =  new Image(getClass().getResource("/university-background.jpg").toExternalForm());
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(1000);
        imageView.setFitHeight(800);
        return imageView;
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
