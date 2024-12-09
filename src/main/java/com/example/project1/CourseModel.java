package com.example.project1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CourseModel {
    private final SimpleIntegerProperty courseId;
    private final SimpleStringProperty courseName;
    private final SimpleStringProperty courseDepartment;
    private final SimpleStringProperty examDate;

    public CourseModel(int courseId, String courseName, String courseDepartment, String examDate) {
        this.courseId = new SimpleIntegerProperty(courseId);
        this.courseName = new SimpleStringProperty(courseName);
        this.courseDepartment = new SimpleStringProperty(courseDepartment);
        this.examDate = new SimpleStringProperty(examDate);
    }

    public int courseId() {
        return courseId.get();
    }

    public String getCourseName() {
        return courseName.get();
    }

    public String getCourseDepartment() {
        return courseDepartment.get();
    }

    public String getExamDate() {
        return examDate.get();
    }

    public SimpleIntegerProperty courseIdProperty() {
        return courseId;
    }

    public SimpleStringProperty courseNameProperty() {
        return courseName;
    }

    public SimpleStringProperty courseDepartmentProperty() {
        return courseDepartment;
    }

    public SimpleStringProperty examDateProperty() {
        return examDate;
    }

    public void setCourseId(int courseId) {
        this.courseId.set(courseId);
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public void setCourseDepartment(String courseDepartment) {
        this.courseDepartment.set(courseDepartment);
    }

    public void setExamDate(String examDate) {
        this.examDate.set(examDate);
    }
}
