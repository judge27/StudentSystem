package com.example.demo1;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private final StringProperty name;
    private final IntegerProperty code;
    private final StringProperty department;
    private final StringProperty grade;

    public Student(String name, int code, String department, String grade) {
        this.name = new SimpleStringProperty(name);
        this.code = new SimpleIntegerProperty(code);
        this.department = new SimpleStringProperty(department);
        this.grade = new SimpleStringProperty(grade);
    }

    // Getters and setters for name
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Getters and setters for code
    public int getCode() {
        return code.get();
    }


    public IntegerProperty codeProperty() {
        return code;
    }

    // Getters and setters for department
    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public StringProperty departmentProperty() {
        return department;
    }

    // Getters and setters for grade
    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public StringProperty gradeProperty() {
        return grade;
    }
}